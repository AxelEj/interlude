package la2.game.net.server.auth;

import net.io.SendablePacket;

public class WaitingLoginPacket extends SendablePacket {
	private String login;
	
	public WaitingLoginPacket(String login) {
		this.login = login;
	}
	
	@Override
	public void write() {
		writeB(0x01);
		
		writeS(login);
	}
}
