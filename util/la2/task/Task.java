package la2.task;

public abstract class Task<C> implements Runnable {
	protected C client;
	
	public void setClient(C client) {
		this.client = client;
	}
}
