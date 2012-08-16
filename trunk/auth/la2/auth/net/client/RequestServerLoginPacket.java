package la2.auth.net.client;

import java.nio.ByteBuffer;

import net.io.RecvablePacket;

public class RequestServerLoginPacket extends RecvablePacket {
	public static final int ID = 0x02;

	private int key1;
	
	private int key2;
	
	private int server;
	
	public RequestServerLoginPacket(ByteBuffer buffer) {
		super(buffer);
		
		key1 = readDW();
		
		key2 = readDW();
		
		server = readB();
	}

	public int getKey1() {
		return key1;
	}

	public int getKey2() {
		return key2;
	}
	
	public int getServerId() {
		return server;
	}
}
