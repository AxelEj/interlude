package la2.auth.net.server;

import java.util.ArrayList;

import la2.auth.AuthConfig;
import la2.auth.GameServer;
import net.io.SendablePacket;

public class ServerListPacket extends SendablePacket {
	private int lastServer;
	
	public ServerListPacket(int lastServer) {
		this.lastServer = lastServer;
	}
	
	@Override
	public void write() {
		writeB(0x04);
		
		ArrayList<GameServer> list = AuthConfig.SERVER_LIST;
		
		writeB(list.size());
		
		writeB(lastServer);
		
		for (GameServer server : list) {
			writeB(server.getServerId()); // server id

			writeB(server.getAddress());
			
			writeDW(server.getPort());
			
			writeB(server.getAgeLimit()); // age limit
			
			writeB(server.isPvp() ? 0x01 : 0x00);
			
			writeW(server.getOnline());
			
			writeW(server.getLimit());
			
			writeB(server.isOnline() ? 0x01 : 0x00);
			
			int bits = 0;
			
			if (server.isTest())
				bits |= 0x04;

			writeDW(bits);
			
			writeB(0x00);
		}
	}

}
