package la2.auth.task.game;

import task.Task;
import la2.auth.AuthGameClient;
import la2.auth.net.client.game.LoginFailPacket;
import la2.auth.AccountManager;

public class LoginFailTask extends Task<AuthGameClient> {
	private LoginFailPacket packet;
	
	public LoginFailTask(LoginFailPacket packet) {
		this.packet = packet;
	}

	@Override
	public void run() {
		synchronized (client.getWaitingList()) {
			client.getWaitingList().remove(packet.getLogin());
			
			AccountManager.getInstance().logout(packet.getLogin());
		}
	}
}
