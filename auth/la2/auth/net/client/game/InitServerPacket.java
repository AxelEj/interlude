package la2.auth.net.client.game;

import java.nio.ByteBuffer;

import net.io.RecvablePacket;

public class InitServerPacket extends RecvablePacket {

	private int id;

	private int ageLimit;
	
	private int limit;
	
	private int online;
	
	private boolean pvp;
	
	private boolean test;
	
	public InitServerPacket(ByteBuffer buffer) {
		super(buffer);
		
		id = readB();
		
		ageLimit = readB();
			
		limit = readW();
		
		online = readW();
		
		pvp = (readB() > 0);
		
		test = (readB() > 0);
	}
	
	public int getServerId() {
		return id;
	}

	public int getAgeLimit() {
		return ageLimit;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public int getOnline() {
		return online;
	}
	
	public boolean isPvp() {
		return pvp;
	}
	
	public boolean isTest() {
		return test;
	}
}