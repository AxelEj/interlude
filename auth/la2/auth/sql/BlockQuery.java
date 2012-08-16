package la2.auth.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import la2.auth.AccountManager;

import sql.Query;

public class BlockQuery extends Query<AccountManager> {
	private static final String QUERY = "{call block_account(?,?,?)}";
	
	private final String login;
	
	private final long block_date;
	
	private final long unblock_date;
	
	public BlockQuery(String login, long block_date, long unblock_date) {
		this.login = login;
		
		this.block_date = block_date;
		
		this.unblock_date = unblock_date;
	}

	@Override
	public void execute(Connection con) throws SQLException {
		CallableStatement stmt = con.prepareCall(QUERY);
		
		stmt.setString(1, login);
		
		stmt.setLong(2, block_date);
		
		stmt.setLong(3, unblock_date);
		
		stmt.executeUpdate();
		
		stmt.close();
	}
}
