package la2.auth.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import la2.auth.AccountManager;
import sql.Query;

public class LoadAccountQuery extends Query<AccountManager>{
	private static final String LOAD_ACCOUNT_QUERY = "SELECT `login`,`password` FROM `account`";
	
	private static final String LOAD_BLOCK_LIST = "SELECT `login`,`block_date`,`unblock_date` FROM `account_block`";

	@Override
	public void execute(Connection con) throws SQLException {
		
		CallableStatement stmt = con.prepareCall(LOAD_ACCOUNT_QUERY);
		
		ResultSet rs = stmt.executeQuery();
		
		while(rs.next())
			AccountManager.getInstance().register(rs.getString(1), rs.getString(2));
		
		rs.close();
		
		stmt.close();
		
		//load block list
		stmt = con.prepareCall(LOAD_BLOCK_LIST);
		
		rs = stmt.executeQuery();

		while(rs.next()) 
			AccountManager.getInstance().blocked(rs.getString(1), rs.getLong(2), rs.getLong(3));
	
		rs.close();
		
		stmt.close();
		
	}

}
