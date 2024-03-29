package la2.auth.task.game;

import task.Task;
import la2.auth.AuthClient;
import la2.auth.AuthGameClient;
import la2.auth.AuthClient.ClientState;
import la2.auth.net.client.game.WaitingLoginPacket;
import la2.auth.net.server.PlaySuccessPacket;

public class WaitingLoginTask extends Task<AuthGameClient>{
	private WaitingLoginPacket packet;
	
	public WaitingLoginTask(WaitingLoginPacket packet) {
		this.packet = packet;
	}

	@Override
	public void run() {
		synchronized (client.getWaitingList()) {
			if(client.getWaitingList().containsKey(packet.getLogin())) {
				AuthClient client = this.client.getWaitingList().get(packet.getLogin());
				
				client.setState(ClientState.PLAY);
				
				client.send(new PlaySuccessPacket(client.getSessionKey()));//TODO send packet LogoutWaitingPacket when lost connection
			}
		}
	}
}
