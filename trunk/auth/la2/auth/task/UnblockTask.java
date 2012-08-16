package la2.auth.task;

import la2.auth.AccountManager;
import task.Task;

public class UnblockTask extends Task<AccountManager> {
	private final String login;
	
	public UnblockTask(String login) {
		this.login = login;
	}
	
	@Override
	public void run() {
		AccountManager.getInstance().unblock(login);
	}

}
