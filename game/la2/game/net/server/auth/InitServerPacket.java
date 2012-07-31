package la2.game.net.server.auth;

import la2.game.GameServer;
import net.io.SendablePacket;

public class InitServerPacket extends SendablePacket {
	private int id;
	
	private int ageLimit;
	
	private int limit;
	
	private int online;
	
	private boolean pvp;
	
	private boolean test;
	
	
	public InitServerPacket(GameServer server) {
		id = server.getServerId();
		
		ageLimit = server.getAgeLimit();
		
		limit = server.getLimit();
		
		online = server.getOnline();
		
		pvp = server.isPvp();
		
		test = server.isTest();
	}
	
	@Override
	public void write() {
		writeB(0x00);
		
		writeB(id);
		
		writeB(ageLimit);
		
		writeW(limit);
		
		writeW(online);
		
		writeB(pvp ? 1 : 0);
		
		writeB(test ? 1 : 0);
	}
	
	
}
