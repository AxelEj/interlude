package la2.game.task;

import java.util.concurrent.Executors;

import task.TaskExecutor;
import task.TaskManager;
import task.TaskScheduler;
import la2.game.GameConfig;

public class GameExecutor {
	private static GameExecutor instance;
	
	public static GameExecutor getInstance() {
		return instance;
	}
	
	private TaskExecutor executor;
	
	private TaskScheduler scheduler;
	
	public GameExecutor() {
		executor = new TaskExecutor(Executors.newFixedThreadPool(GameConfig.TASK_THREADS_COUNT));
		
		scheduler = new TaskScheduler(GameConfig.TASK_SCHEDULER_THREADS_COUNT);
		
		instance = this;
	}
	
	public <Client> TaskManager<Client> create(Client client) {
		return new TaskManager<Client>(client, executor, scheduler);
	}
}
