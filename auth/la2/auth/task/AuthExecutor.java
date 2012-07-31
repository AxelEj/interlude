package la2.auth.task;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import la2.auth.AuthClient;
import la2.auth.AuthGameClient;
import la2.task.TaskExecutor;
import la2.task.TaskManager;

public class AuthExecutor {
	private static AuthExecutor instance;
	
	public static AuthExecutor getInstance() {
		return instance;
	}
	
	private Executor executor;
	
	private TaskExecutor<AuthClient> auth;
	
	private TaskExecutor<AuthGameClient> game;
	
	public AuthExecutor(int count) {
		executor = Executors.newFixedThreadPool(count);

		auth = new TaskExecutor<AuthClient>(executor);
	
		game = new TaskExecutor<AuthGameClient>(executor);
		
		instance = this;
	}
	
	public TaskManager<AuthClient> create(AuthClient client) {
		return auth.create(client);
	}
	
	public TaskManager<AuthGameClient> create(AuthGameClient client) {
		return game.create(client);
	}
	
}