package la2.auth.task;

import java.util.concurrent.Executors;

import la2.auth.AuthConfig;
import task.TaskExecutor;
import task.TaskManager;
import task.TaskScheduler;

public class AuthExecutor {
	public static class SingletonHolder {
		public static final AuthExecutor instance = new AuthExecutor();	
	}
	
	public static AuthExecutor getInstance() {
		return SingletonHolder.instance;
	}
	
	private TaskExecutor executor;
	
	private TaskScheduler scheduler;
	
	public AuthExecutor() {
		executor = new TaskExecutor(Executors.newFixedThreadPool(AuthConfig.TASK_THREADS_COUNT));
	
		scheduler = new TaskScheduler(AuthConfig.TASK_SCHEDULER_THREADS_COUNT);
	}
	
	public <Client> TaskManager<Client> create(Client client) {
		return new TaskManager<Client>(client, executor, scheduler);
	}
}
