package la2.auth.task;

import task.Task;
import la2.auth.AuthClient;
import la2.auth.AuthClient.ClientState;
import la2.auth.net.client.AuthGameGuardPacket;
import la2.auth.net.server.GameGuardAuthPacket;
import la2.auth.net.server.LoginFailPacket;
import la2.auth.net.server.LoginFailPacket.LoginFailReason;

public class AuthGameGuardTask extends Task<AuthClient> {
	private int session;
	
	public AuthGameGuardTask(AuthGameGuardPacket packet) {
		session = packet.getSession();
	}

	@Override
	public void run() {
		if(client.getState().equals(ClientState.INIT) && session == client.getSession()) {
			client.setState(ClientState.AUTH_GAME_GUARD);
			
			client.send(new GameGuardAuthPacket(client));
		} else 
			client.close(new LoginFailPacket(LoginFailReason.REASON_ACCESS_FAILED));
	}

}