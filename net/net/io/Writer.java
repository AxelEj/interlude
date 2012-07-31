package net.io;

import java.nio.ByteBuffer;

public interface Writer {

	public void write(ByteBuffer buffer,SendablePacket packet);
	
}
