package la2.auth.net.client;

import java.nio.ByteBuffer;

import net.io.RecvablePacket;
 
public class RequestServerListPacket extends RecvablePacket {
	public static final int ID = 0x05;

	private int key1;
	
	private int key2;

	public RequestServerListPacket(ByteBuffer buffer) {
		super(buffer);

		key1 = readDW();
		
		key2 = readDW();
	}
	
	public int getKey1() {
		return key1;
	}


	public int getKey2() {
		return key2;
	}

}
