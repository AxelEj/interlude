package la2.auth.task.game;

import task.Task;
import la2.auth.AccountManager;
import la2.auth.AuthGameClient;
import la2.auth.net.client.game.LogoutPacket;

public class LogoutTask extends Task<AuthGameClient> {
	private LogoutPacket packet;
	
	public LogoutTask(LogoutPacket packet) {
		this.packet = packet;
	}
	
	@Override
	public void run() {
		AccountManager.getInstance().logout(packet.getLogin());
	}

}
