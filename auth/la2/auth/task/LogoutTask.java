package la2.auth.task;

import la2.auth.AccountManager;
import la2.auth.AuthClient;
import task.Task;

public class LogoutTask extends Task<AuthClient> {
	private String login;
	
	public LogoutTask(String login) {
		this.login = login;
	}
	
	@Override
	public void run() {
		AccountManager.getInstance().logout(login);
	}

}
