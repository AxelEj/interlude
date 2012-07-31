package la2.sql;

import java.sql.DriverManager;
import java.sql.SQLException;

import la2.util.Config;

public class DatabaseManager {
	private static DatabaseManager instance;
	
	public static DatabaseManager getInstance() {
		return instance;
	}

	private String url;
	
	public DatabaseManager(Config config) {
		try {
			Class.forName(config.getString("SQL_DRIVER","com.mysql.jdbc.Driver")).newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuilder str = new StringBuilder();
		
		str.append(config.getString("SQL_URL", "jdbc:mysql:")).append("//");
		
		str.append(config.getString("SQL_HOST", "localhost")).append("/");
		
		str.append(config.getString("SQL_DB","db")).append('?');
		
		str.append("user=").append(config.getString("SQL_USER", "root")).append('&');
		
		str.append("password").append(config.getString("SQL_PASS",""));
		
		url = str.toString();
		
		instance = this;
	}

	public java.sql.Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url);
	}
}
