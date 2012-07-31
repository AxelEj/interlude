package la2.auth.task.game;

import la2.auth.AuthGameClient;
import la2.auth.net.client.game.LoginFailPacket;
import la2.auth.util.AccountManager;
import la2.task.Task;

public class LoginFailTask extends Task<AuthGameClient> {
	private LoginFailPacket packet;
	
	public LoginFailTask(LoginFailPacket packet) {
		this.packet = packet;
	}

	@Override
	public void run() {
		synchronized (client.getWaitingList()) {
			client.getWaitingList().remove(packet.getLogin());
			
			AccountManager.getInstance().remove(packet.getLogin());
		}
	}
}
