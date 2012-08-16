package la2.auth;

import java.util.HashMap;

import la2.auth.sql.AuthDatabase;
import la2.auth.sql.BlockQuery;
import la2.auth.sql.LoadAccountQuery;
import la2.auth.sql.LoginQuery;
import la2.auth.sql.LogoutQuery;
import la2.auth.sql.UnblockQuery;
import la2.auth.task.AuthExecutor;
import la2.auth.task.UnblockTask;
import sql.QueryManager;
import task.TaskManager;

public class AccountManager {
	public static final int LOGIN_SUCCESS = 0;

	public static final int ACCOUNT_NOT_FOUND = 1;
	
	public static final int ACCOUNT_WRONG_PASSWORD = 2;
	
	public static final int ACCOUNT_IN_USE = 3;
	
	public static final int ACCOUNT_BLOCKED = 4;
	
	private static AccountManager instance;
	
	public static AccountManager getInstance() {
		return instance;
	}
	
	public class Account {
		public final String login;
		
		public final String password;
		
		public boolean block = false;
		
		public boolean logined = false;
		
		public Account(String login,String password) {
			this.login = login;
			
			this.password = password;
		}
	}
	
	private HashMap<String, Account> accountsList = new HashMap<String, Account>();
	
	private QueryManager<AccountManager> database;
	
	private TaskManager<AccountManager> executor;
	
	public AccountManager() {
		database = AuthDatabase.getInstance().create(this);
		
		database.execute(new LoadAccountQuery());
		
		executor = AuthExecutor.getInstance().create(this);
		
		instance = this;
	}
	
	public void register(String login,String password) {
		Account account = new Account(login, password);
		
		synchronized (accountsList) {
			accountsList.put(login, account);
		}
	}
	
	public int login(String login,String password, String host) {
		Account account = accountsList.get(login);

		if(account != null) {
			synchronized (account) {
				if(account.block) 
					return ACCOUNT_BLOCKED;
					
				if(account.logined)
					return ACCOUNT_IN_USE;
					
				if(account.password.equals(password)) {
					account.logined = true;
					
					database.execute(new LoginQuery(login, System.currentTimeMillis(), host));
						
					return LOGIN_SUCCESS;
				} else
					return ACCOUNT_WRONG_PASSWORD;
			}	
		} else
			return ACCOUNT_NOT_FOUND;
	}
	
	public void logout(String login) {
		Account account = accountsList.get(login);
			
		if(account != null) {
			synchronized (account) {
				account.logined = false;
					
				database.execute(new LogoutQuery(login, System.currentTimeMillis()));
			}
		} 
	}
	
	public void block(String login,long block_date,long unblock_date) {
		Account account = accountsList.get(login);
		
		if(account != null) {
			synchronized (account) {
				account.block = true;
				
				if(unblock_date > 0)
					executor.schedule(new UnblockTask(login), (unblock_date - block_date));
				
				database.execute(new BlockQuery(login, block_date, unblock_date));
			}	
		}
		
	}
	
	public void blocked(String login, long block_date, long unblock_date) {
		Account account = accountsList.get(login);
		
		if(account != null) {
			synchronized (account) {
				account.block = true;
				
				if(unblock_date > 0)
					executor.schedule(new UnblockTask(login), (unblock_date - block_date));
			}	
		}
	}
	
	public void unblock(String login) {
		Account account = accountsList.get(login);
		
		if(account != null) {
			synchronized (account) {
				if(account.block) {
					account.block = false;
					
					database.execute(new UnblockQuery(login));	
				}
			}	
		}		
	}
	
}
