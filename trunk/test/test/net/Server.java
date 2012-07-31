package test.net;

import java.nio.channels.SocketChannel;

import net.Factory;
import net.Filter;
import net.SelectionClient;
import net.SelectionServer;

public class Server extends SelectionServer
	implements Factory,Filter
{

	public Server(String host, int port) {
		super(host, port);
		
		setFactory(this);
		
		setFilter(this);
	}

	@Override
	public boolean accept(SocketChannel sc) {
		return true;
	}

	@Override
	public SelectionClient create() {
		return new Client();
	}

}
