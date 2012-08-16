package la2.auth.task.game;

import task.Task;
import la2.auth.AuthGameClient;
import la2.auth.AuthGameServer;
import la2.auth.GameServer;
import la2.auth.net.client.game.InitServerPacket;
public class InitServerTask extends Task<AuthGameClient> {
	private InitServerPacket packet;
	
	public InitServerTask(InitServerPacket packet) {
		this.packet = packet;
	}

	@Override
	public void run() {
		GameServer server = AuthGameServer.getInstance().getServer(packet.getServerId());
		
		if(server != null) {
			client.setServer(server);
			
			server.setAgeLimit(packet.getAgeLimit());
			
			server.setLimit(packet.getLimit());
			
			server.setOnline(packet.getOnline());
			
			server.setPvp(packet.isPvp());
			
			server.setServer(client);
			
			server.setTest(packet.isTest());
		} else 
			client.close();
	}
	
}
