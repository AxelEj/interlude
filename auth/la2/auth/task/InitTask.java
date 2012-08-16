package la2.auth.task;

import task.Task;
import la2.auth.AuthClient;
import la2.auth.AuthClient.ClientState;
import la2.auth.net.server.InitPacket;

public class InitTask extends Task<AuthClient> {

	@Override
	public void run() {
		client.setState(ClientState.INIT);
		
		client.send(new InitPacket(client));
	}

}
