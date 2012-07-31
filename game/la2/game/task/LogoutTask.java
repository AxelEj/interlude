package la2.game.task;

import la2.game.AuthClient;
import la2.game.GameClient;
import la2.game.net.server.auth.LogoutPacket;
import la2.task.Task;

public class LogoutTask extends Task<GameClient>{
	private String login;
	
	public LogoutTask(String login) {
		this.login = login;
	}
	
	@Override
	public void run() {
		AuthClient.getInstance().send(new LogoutPacket(login));
	}

}
