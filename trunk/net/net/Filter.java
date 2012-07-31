package net;

import java.nio.channels.SocketChannel;

public interface Filter {

	public boolean accept(SocketChannel sc);
	
}
