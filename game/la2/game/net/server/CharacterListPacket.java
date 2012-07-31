package la2.game.net.server;

import la2.game.GameClient;
import net.io.SendablePacket;

public class CharacterListPacket extends SendablePacket {
	private GameClient client;
	
	public CharacterListPacket(GameClient client) {
		this.client = client;
	}
	
	@Override
	public void write() {
		writeB(0x13);
		
		writeDW(0x00);
	}

}
