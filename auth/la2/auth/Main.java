package la2.auth;

import java.io.IOException;
import java.security.GeneralSecurityException;

import net.Controller;

public class Main {
	private static Controller controller;
	
	public static Controller getController() {
		return controller;
	}
	
	public static void main(String...args) {
		System.out.println("Load server config...");
		
		new AuthConfig();
		
		System.out.println("Load account manager...");
		
		new AccountManager();

		try {
			controller = new Controller();
			
			controller.register(new AuthServer(AuthConfig.AUTH_HOST, AuthConfig.AUTH_PORT));

			controller.register(new AuthGameServer(AuthConfig.GAME_HOST, AuthConfig.GAME_PORT));
			
			controller.start();
		
			System.out.println("Server started...");
		} catch (IOException e) { 
			e.printStackTrace();
		} catch (GeneralSecurityException e) { 
			e.printStackTrace();
		}
	}
}
