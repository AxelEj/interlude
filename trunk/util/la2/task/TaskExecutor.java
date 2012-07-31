package la2.task;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class TaskExecutor<C> {
	private Executor executor;
	
	public TaskExecutor(int count) {
		executor = Executors.newFixedThreadPool(count);
	}
	
	public TaskExecutor(Executor executor) {
		this.executor = executor;
	}
	
	public void execute(Task<C> task) {
		executor.execute(task);
	}
	
	public TaskManager<C> create(C client) {
		return new TaskManager<C>(this, client);
	}
}
