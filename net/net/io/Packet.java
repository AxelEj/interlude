package net.io;

import java.nio.ByteBuffer;

public abstract class Packet {
	protected ByteBuffer buffer;
	
	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}
}
