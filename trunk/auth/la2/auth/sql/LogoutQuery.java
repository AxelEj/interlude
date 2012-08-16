package la2.auth.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import la2.auth.AccountManager;

import sql.Query;

public class LogoutQuery extends Query<AccountManager> {
	private static final String QUERY = "{call logout(?,?)}";
	
	private final String login;
	
	private final long time;
	
	public LogoutQuery(String login,long time) {
		this.login = login;
		
		this.time = time;
	}

	@Override
	public void execute(Connection con) throws SQLException {
		CallableStatement stmt = con.prepareCall(QUERY);
		
		stmt.setString(1, login);
		
		stmt.setLong(2, time);
		
		stmt.executeUpdate();
		
		stmt.close();
	}
	
}
