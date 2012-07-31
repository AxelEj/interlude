package la2.auth.task.game;

import la2.auth.AuthGameClient;
import la2.auth.util.AccountManager;
import la2.auth.net.client.game.LogoutPacket;
import la2.task.Task;

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
