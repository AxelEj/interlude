package la2.auth.task.game;

import la2.auth.AuthGameClient;
import la2.auth.net.client.game.LoginSuccessPacket;
import la2.task.Task;

public class LoginSuccessTask extends Task<AuthGameClient> {
	private LoginSuccessPacket packet;
	
	public LoginSuccessTask(LoginSuccessPacket packet) {
		this.packet = packet;
	}
	
	@Override
	public void run() {
		synchronized (client.getWaitingList()) {//TODO add to server login list
			client.getWaitingList().remove(packet.getLogin());
		}
	}

}
