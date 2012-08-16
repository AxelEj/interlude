package la2.game;

import java.io.IOException;

import la2.game.task.GameExecutor;

import net.Controller;

public class Main {
	
	public static void main(String...args) {
		System.out.println("Load config...");
		
		new GameConfig();
		
		new GameExecutor();//TODO
		
		try {
			Controller controller = new Controller();
			
			controller.register(new AuthClient(GameConfig.AUTH_HOST, GameConfig.AUTH_PORT));
			
			controller.register(new GameServer(GameConfig.GAME_HOST, GameConfig.GAME_PORT));
			
			controller.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
