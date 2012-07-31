package la2.auth.net.server;

import la2.auth.AuthClient;
import la2.util.SessionKey;
import net.io.SendablePacket;

public class LoginSuccessPacket extends SendablePacket {
	private SessionKey key;

	public LoginSuccessPacket(AuthClient client) {
		this(client.getSessionKey());
	}
	
	public LoginSuccessPacket(SessionKey key) {
		this.key = key;
	}
	
	@Override
	public void write() {
		writeB(0x03);
		writeDW(key.getKey1());
		writeDW(key.getKey2());
		writeDW(0x00);
		writeDW(0x00);
		writeDW(0x000003ea);
		writeDW(0x00);
		writeDW(0x00);
		writeDW(0x00);
		writeQ(0);writeQ(0);
		
	}

	
}
