package la2.auth.net.server;

import la2.util.SessionKey;
import net.io.SendablePacket;

public final class PlaySuccessPacket extends SendablePacket {
	private int key3;
	
	private int key4;

	public PlaySuccessPacket(SessionKey key) {
		key3 = key.getKey3();
		
		key4 = key.getKey4();
	}
	
	@Override
	public void write() {
		writeB(0x07);
		
		writeDW(key3);
		
		writeDW(key4);
	}
}
