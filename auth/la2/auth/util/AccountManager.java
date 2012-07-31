package la2.auth.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;

import la2.sql.DatabaseManager;

public class AccountManager {
	private static final String LOGIN = "{? = call login(?,?,FROM_UNIXTIME(?),?)}";
	
	private static final String LOGOUT = "{call logout(?,FROM_UNIXTIME(?))}";
	
	private static AccountManager instance;
	
	private HashSet<String> list = new HashSet<String>();
	
	private MessageDigest md5;
	
	public static AccountManager getInstance() {
		return instance;
	}
	
	public AccountManager() throws NoSuchAlgorithmException {
		md5 = MessageDigest.getInstance("MD5");
		
		instance = this;
	}
	
	/**
	 * 
	 * @param login
	 * @param password
	 * @param ip
	 * @return 0 login success
	 *		   -4 login already use
	 *		   -1 login wrong login
	 *		   -2 login blocked
	 * 		   -3 login wrong password
	 */
	public int login(String login,String password,String ip) {
		synchronized(list) {
			if(!list.contains(login)) {
				try {
					Connection con = DatabaseManager.getInstance().getConnection();
					
					CallableStatement cs = con.prepareCall(LOGIN);
					
					cs.registerOutParameter(1, Types.TINYINT);
						
					cs.setString(2, login);
					
					md5.reset();
					
					md5.update(password.getBytes());
					
					BigInteger hash = new BigInteger(1,md5.digest());
					
					cs.setString(3, hash.toString(16));
					
					cs.setLong(4, System.currentTimeMillis());
					
					cs.setString(5, ip);
					
					cs.execute();
					
					int result = cs.getInt(1);
					
					cs.close();
					
					con.close();
					
					if(result == 0)
						list.add(login);
					

					return result;
				} catch (SQLException e) {
					e.printStackTrace();
					
					return -1;
				} 
			} else {
				return -4;
			}
		}
	}
	
	public void logout(String login) {
		synchronized(list) {
			try {
				Connection con = DatabaseManager.getInstance().getConnection();
				
				CallableStatement cs = con.prepareCall(LOGOUT);
								
				cs.setString(1, login);
				
				cs.setLong(2, System.currentTimeMillis());
				
				cs.execute();

				cs.close();
				
				con.close();
				
				list.remove(login);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void remove(String login) {
		synchronized(list) {
			list.remove(login);
		}
	}
}
