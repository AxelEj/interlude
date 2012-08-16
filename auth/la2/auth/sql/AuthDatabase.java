package la2.auth.sql;


import la2.auth.AuthConfig;
import sql.Database;

public class AuthDatabase {
	public static class SingletonHolder {
		public static final AuthDatabase instance = new AuthDatabase();	
	}
	
	public static Database getInstance() {
		return SingletonHolder.instance.database;
	}

	private final Database database;
	
	public AuthDatabase() {
		database = new Database(AuthConfig.SQL_DRIVER, AuthConfig.SQL_URL);
	}
}
