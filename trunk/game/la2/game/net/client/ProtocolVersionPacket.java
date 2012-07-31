package la2.game.net.client;

import java.nio.ByteBuffer;

import net.io.RecvablePacket;

public class ProtocolVersionPacket extends RecvablePacket {
	private int version;
	
	public ProtocolVersionPacket(ByteBuffer buffer) {
		super(buffer);

		version = readDW();
	}
	
	public int getVersion() {
		return version;
	}
}
