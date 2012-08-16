package la2.auth.net.server.game;

import la2.auth.AuthClient;
import la2.util.SessionKey;
import net.io.SendablePacket;

public class LoginPacket extends SendablePacket {
	private String login;
	
	private SessionKey key;
	
	public LoginPacket(AuthClient client) {
		this(client.getLogin(),client.getSessionKey());
	}
	
	public LoginPacket(String login,SessionKey key) {
		this.login = login;
		
		this.key = key;
	}
	
	@Override
	public void write() {
		writeB(0x00);
		
		writeS(login);
		
		writeDW(key.getKey1());
		
		writeDW(key.getKey2());
		
		writeDW(key.getKey3());
		
		writeDW(key.getKey4());
	}

}
