package la2.auth.net.client.game;

import java.nio.ByteBuffer;

import net.io.RecvablePacket;

public class LoginFailPacket extends RecvablePacket{
	private String login;
	
	public LoginFailPacket(ByteBuffer buffer) {
		super(buffer);
		
		login = readS();
	}
	
	public String getLogin() {
		return login;
	}
}
