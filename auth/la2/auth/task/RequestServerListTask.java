package la2.auth.task;

import la2.auth.AuthClient;
import la2.auth.AuthClient.ClientState;
import la2.auth.net.client.RequestServerListPacket;
import la2.auth.net.server.LoginFailPacket;
import la2.auth.net.server.LoginFailPacket.LoginFailReason;
import la2.auth.net.server.ServerListPacket;
import la2.task.Task;

public class RequestServerListTask extends Task<AuthClient> {
	RequestServerListPacket packet;
	
	public RequestServerListTask(RequestServerListPacket packet) {
		this.packet = packet;
	}
	
	@Override
	public void run() {
		if(client.getState().equals(ClientState.LOGIN)) {
			if(client.getSessionKey().getKey1() == packet.getKey1() && client.getSessionKey().getKey2() == packet.getKey2()) {
				client.setState(ClientState.SELECT_SERVER);
				
				client.send(new ServerListPacket(0x0f));
			} else
				client.close(new LoginFailPacket(LoginFailReason.REASON_ACCESS_FAILED));
		} else
			client.close(new LoginFailPacket(LoginFailReason.REASON_ACCESS_FAILED));
	}
	
}
