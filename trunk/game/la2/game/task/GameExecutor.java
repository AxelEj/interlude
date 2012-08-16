package la2.game.task;

import la2.game.GameClient;
import la2.task.TaskExecutor;
import la2.task.TaskManager;

public class GameExecutor {
	private static GameExecutor instance;
	
	public static GameExecutor getInstance() {
		return instance;
	}
	
	private Task<GameClient> executor;
	
	public GameExecutor(int count) {
		executor = new TaskExecutor<GameClient>(count);
		
		instance = this;
	}
	
	public TaskManager<GameClient> create(GameClient client) {
		return executor.create(client);
	}
}
