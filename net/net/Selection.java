package net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

public abstract class Selection {
	protected SelectionKey key;
	
	protected SelectableChannel sc;
	
	protected InetSocketAddress addr;
	
	protected Controller controller;
	
	public abstract void connect();
	
	public void cancel() {
		if(key.isValid()) {
			try {
				sc.close();
			} catch (IOException e) { }
			
			key.cancel();
			
			key = null;
			
			sc = null;
		}
	}
	public SelectableChannel getChannel() {
		return sc;
	}
	
	public InetAddress getInetAddress() {
		return addr.getAddress();
	}
	public abstract void tryRead(ByteBuffer buffer);

	public abstract void register(Controller controller) throws IOException;
	
	public void setKey(SelectionKey key) {
		this.key = key;
	}
	
	public void setChannel(SelectableChannel sc) {
		this.sc = sc;
	}
	
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	public abstract void tryWrite(ByteBuffer buffer);
	
}
