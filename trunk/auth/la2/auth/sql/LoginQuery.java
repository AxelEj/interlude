package la2.auth.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import la2.auth.AccountManager;

import sql.Query;

public class LoginQuery extends Query<AccountManager> {
	private static final String QUERY = "{call login(?,?,?)}";
	
	private final String login;
	
	private final long time;
	
	private final String host;
	
	public LoginQuery(String login,long time, String host) {
		this.login = login;
		
		this.time = time;
		
		this.host = host;
	}

	@Override
	public void execute(Connection con) throws SQLException {
		CallableStatement stmt = con.prepareCall(QUERY);
		
		stmt.setString(1, login);
		
		stmt.setLong(2, time);
		
		stmt.setString(3, host);
		
		stmt.executeUpdate();
		
		stmt.close();
	}
}