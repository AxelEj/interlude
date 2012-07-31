package net.io;

import java.nio.ByteBuffer;

public abstract class RecvablePacket extends Packet {
	
	public RecvablePacket(ByteBuffer buffer) {
		setBuffer(buffer);
	}
	
	protected final int readB() {
		return buffer.get();
	}
	
	protected final void readB(byte[] value) {
		buffer.get(value);
	}
	
	protected final int readW() {
		return buffer.getShort();
	}
	
	protected final int readDW() {
		return buffer.getInt();
	}
	
	protected final long readQ() {
		return buffer.getLong();
	}
	
	protected final float readF() {
		return buffer.getFloat();
	}
	
	protected final double readD() {
		return buffer.getDouble();
	}
	
	protected String readS() {
		StringBuilder str = new StringBuilder();
		
		char c;
		
		while((c = buffer.getChar()) != '\0')
			str.append(c);
		
		return str.toString();
	}
}
