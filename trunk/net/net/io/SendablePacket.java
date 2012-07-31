package net.io;

public abstract class SendablePacket extends Packet {
	
	protected final void writeB(final int value) {
		buffer.put((byte) value);
	}
	
	protected final void writeB(final byte[] value) {
		buffer.put(value);
	}
	
	protected final void writeW(final int value) {
		buffer.putShort((short) value);
	}
	
	protected final void writeDW(final int value) {
		buffer.putInt(value);
	}
	
	protected final void writeQ(final long value) {
		buffer.putLong(value);
	}
	
	protected final void writeF(final float value) {
		buffer.putFloat(value);
	}
	
	protected final void writeD(final double value) {
		buffer.putDouble(value);
	}
	
	protected final void writeS(final String str) {
		for(int i = 0 ; i < str.length() ; i++)
			buffer.putChar(str.charAt(i));
		
		buffer.putChar('\0');
	}
	
	public abstract void write();
}
