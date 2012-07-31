package la2.game.net.client.auth;

import java.nio.ByteBuffer;

import net.io.RecvablePacket;
import la2.util.SessionKey;

public class LoginPacket extends RecvablePacket {
	private String login;
	
	private SessionKey key;
	
	public LoginPacket(ByteBuffer buffer) {
		super(buffer);
		
		login = readS();
		
		key = new SessionKey(readDW(),readDW(),readDW(),readDW());
	}
	
	public String getLogin() {
		return login;
	}
	
	public SessionKey getKey() {
		return key;
	}
}
