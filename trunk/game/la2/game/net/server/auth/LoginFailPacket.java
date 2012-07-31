package la2.game.net.server.auth;

import net.io.SendablePacket;

public class LoginFailPacket extends SendablePacket {
	private String login;
	
	public LoginFailPacket(String login) {
		this.login = login;
	}
	@Override
	public void write() {
		writeB(0x03);
		
		writeS(login);
	}

}
