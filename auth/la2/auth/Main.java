package la2.auth;

import java.io.IOException;
import java.security.GeneralSecurityException;

import la2.auth.task.AuthExecutor;
import la2.auth.util.AccountManager;
import la2.sql.DatabaseManager;
import la2.util.Config;

import net.Controller;

public class Main {
	
	public static void main(String...args) {
		try {
			Config config = new Config("config.ini");
			
			new DatabaseManager(config);
			
			new AccountManager();
			
			new AuthExecutor(20);
		
			Controller controller = new Controller();
			
			controller.register(new AuthServer("127.0.0.1", 2106));
			
			controller.register(new AuthGameServer("127.0.0.1", 9014));
			
			controller.start();
		} catch (IOException e) { 
			e.printStackTrace();
		} catch (GeneralSecurityException e) { 
			e.printStackTrace();
		}
	}
}
