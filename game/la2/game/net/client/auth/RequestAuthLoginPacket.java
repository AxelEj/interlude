package la2.game.net.client.auth;

import java.nio.ByteBuffer;

import net.io.RecvablePacket;
import la2.util.SessionKey;

public class RequestAuthLoginPacket extends RecvablePacket{
	private String login;
	
	private SessionKey key;
	
	public RequestAuthLoginPacket(ByteBuffer buffer) {
		super(buffer);
		
		login = readS();
		
		int key1,key2,key3,key4;
		
		key4 = readDW();
		
		key3 = readDW();
		
		key1 = readDW();
		
		key2 = readDW();
			
		key = new SessionKey(key1,key2,key3,key4);
	}
	
	public String getLogin() {
		return login;
	}
	
	public SessionKey getSessionKey() {
		return key;
	}
}
