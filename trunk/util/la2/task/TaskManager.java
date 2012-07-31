package la2.task;

public class TaskManager<C> {
	private TaskExecutor<C> executor;
	
	private C client;
	
	public TaskManager(TaskExecutor<C> executor,C client) {
		this.executor = executor;
		
		this.client = client;
	}
	
	public void execute(Task<C> task) {
		task.setClient(client);
		
		executor.execute(task);
	}
}
