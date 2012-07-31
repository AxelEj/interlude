package la2.game.net.server.auth;

import net.io.SendablePacket;

public class LoginSuccessPacket extends SendablePacket {
	private String login;
	
	public LoginSuccessPacket(String login) {
		this.login = login;
	}
	
	@Override
	public void write() {
		writeB(0x02);
		
		writeS(login);
	}

}
