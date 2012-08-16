package la2.auth.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import la2.auth.AccountManager;
import sql.Query;

public class UnblockQuery extends Query<AccountManager> {
	private static final String QUERY = "{call unblock_account(?)}";
	
	private final String login;
	
	public UnblockQuery(String login) {
		this.login = login;
	}
	
	@Override
	public void execute(Connection con) throws SQLException {
		CallableStatement stmt = con.prepareCall(QUERY);
		
		stmt.setString(1, login);
		
		stmt.executeUpdate();
		
		stmt.close();
	}
}
