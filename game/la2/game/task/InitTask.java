package la2.game.task;

import task.Task;
import la2.game.GameClient;
import la2.game.net.client.ProtocolVersionPacket;
import la2.game.net.server.CryptInitPacket;

public class InitTask extends Task<GameClient> {
	private ProtocolVersionPacket packet;
	
	public InitTask(ProtocolVersionPacket packet) {
		this.packet = packet;
	}
	
	@Override
	public void run() {
		//TODO check protocol version
		client.send(new CryptInitPacket(client.getCryptKey()));
	}

}
