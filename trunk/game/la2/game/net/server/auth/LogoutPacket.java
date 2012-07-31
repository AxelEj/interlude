package la2.game.net.server.auth;

import net.io.SendablePacket;

public class LogoutPacket extends SendablePacket {
	private String login;
	
	public LogoutPacket(String login) {
		this.login = login;
	}
	
	@Override
	public void write() {
		writeB(0x04);
		
		writeS(login);
	}

}
