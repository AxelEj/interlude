package test.server;

import java.io.IOException;
import java.security.GeneralSecurityException;

import la2.game.AuthClient;
import la2.game.GameServer;
import la2.game.task.GameExecutor;
import la2.sql.DatabaseManager;
import la2.util.Config;

import la2.auth.AuthGameServer;
import la2.auth.AuthServer;
import la2.auth.task.AuthExecutor;
import la2.auth.util.AccountManager;

import net.Controller;

public class Test {
	
	public static void main(String...args) {
		try {
			Config config = new Config("config.ini");
			
			new DatabaseManager(config);
			
			new AccountManager();
			
			new AuthExecutor(20);
			
			new GameExecutor(20);
			
			Controller controller = new Controller();
			
			controller.start();
			
			controller.register(new AuthServer("127.0.0.1",2106));
			
			controller.register(new AuthGameServer("127.0.0.1",9014));
			
			controller.register(new GameServer(config,"127.0.0.1", 7777));
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) { }
			
			controller.register(new AuthClient("127.0.0.1",9014));
			
			System.out.println("started...");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
