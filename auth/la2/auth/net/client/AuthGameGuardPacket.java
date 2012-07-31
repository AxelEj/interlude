package la2.auth.net.client;

import java.nio.ByteBuffer;

import net.io.RecvablePacket;

public class AuthGameGuardPacket extends RecvablePacket {
	private int session;
	
	private int data1;
	
	private int data2;
	
	private int data3;
	
	private int data4;
	
	public AuthGameGuardPacket(ByteBuffer buffer) {
		super(buffer);
		
		session = readDW();
		
		data1 = readDW();
		
		data2 = readDW();
		
		data3 = readDW();
		
		data4 = readDW();
	}
	
	public int getSession() {
		return session;
	}

	public int getData1() {
		return data1;
	}

	public int getData2()
	{
		return data2;
	}

	public int getData3() {
		return data3;
	}

	public int getData4() {
		return data4;
	}

}
