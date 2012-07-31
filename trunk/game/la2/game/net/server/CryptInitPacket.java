package la2.game.net.server;

import net.io.SendablePacket;

public class CryptInitPacket extends SendablePacket {
	private byte[] key;
	
	public CryptInitPacket(byte[] key) {
		this.key = key;
	}
	
	@Override
	public void write() {
		writeB(0x00);
		
		writeB(0x01);
		
		writeB(key);
		
		writeDW(0x01);
		
		writeDW(0x01);		
	}

}
