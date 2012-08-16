package la2.auth.net.server;

import net.io.SendablePacket;
import la2.auth.AuthClient; 

public class GameGuardAuthPacket extends SendablePacket {
	private int session;
	
	public GameGuardAuthPacket(AuthClient client) {
		session = client.getSession();
	}
	
	@Override
	public void write() {
		writeB(0x0b);
        writeDW(session);
        writeDW(0x00);
        writeDW(0x00);
        writeDW(0x00);
        writeDW(0x00);	
	}

}
