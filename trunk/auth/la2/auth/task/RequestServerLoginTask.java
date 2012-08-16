package la2.auth.task;

import task.Task;
import la2.auth.AuthClient;
import la2.auth.AuthGameServer;
import la2.auth.AuthClient.ClientState;
import la2.auth.GameServer;
import la2.auth.net.client.RequestServerLoginPacket;
import la2.auth.net.server.LoginFailPacket;
import la2.auth.net.server.LoginFailPacket.LoginFailReason;
import la2.auth.net.server.PlayFailPacket.PlayFailReason;
import la2.auth.net.server.PlayFailPacket;
import la2.auth.net.server.game.LoginPacket;

public class RequestServerLoginTask extends Task<AuthClient> {
	private RequestServerLoginPacket packet;

	public RequestServerLoginTask(RequestServerLoginPacket packet) {
		this.packet = packet;
	}

	public static int id = 0;
	
	@Override
	public void run() {
		if(client.getState().equals(ClientState.SELECT_SERVER)) {
			if(client.getSessionKey().getKey1() == packet.getKey1() && client.getSessionKey().getKey2() == packet.getKey2()) {
				GameServer server = AuthGameServer.getInstance().getServer(packet.getServerId());
				
				if(server != null) {
					if(server.isOnline()) {
						if(server.getLimit() > server.getOnline()) {
							server.getServer().send(new LoginPacket(client));
							
							synchronized (server.getServer().getWaitingList()) {
								server.getServer().getWaitingList().put(client.getLogin(),client);
							}
						} else
							client.close(new PlayFailPacket(PlayFailReason.REASON_TOO_MANY_PLAYERS));
					} else
						client.close(new LoginFailPacket(LoginFailReason.NULL));
				} else
					client.close(new LoginFailPacket(LoginFailReason.REASON_SYSTEM_ERROR));
				
			} else
				client.close(new LoginFailPacket(LoginFailReason.REASON_ACCESS_FAILED));
		} else 
			client.close(new LoginFailPacket(LoginFailReason.REASON_ACCESS_FAILED));
	}
	
}
