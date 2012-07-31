package net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SelectionServer extends Selection {
	private Factory factory;
	
	private Filter filter;
	
	public SelectionServer(String host, int port) {
		addr = new InetSocketAddress(host, port);
	}

	public final void accept() {
		if(key.isValid()) {
			ServerSocketChannel ssc = (ServerSocketChannel) sc;
			
			SocketChannel sc;
			
			for(int i = 0 ; i < 10 ; i++) {//TODO config
				try {
					if((sc = ssc.accept()) != null) {
						if(filter.accept(sc)) {
							SelectionClient client = factory.create();
							
							client.setChannel(sc);
							
							client.register(controller);
						} else
							try {
								sc.close();
							} catch(IOException e) { }
					} else
						return;
				} catch (IOException e) {
					cancel();
					
					return;
				}
			}
		}
	}
	
	@Override
	public void register(Controller controller) throws IOException {
		ServerSocketChannel sc = ServerSocketChannel.open();
		
		sc.configureBlocking(false);
		
		setChannel(sc);
		
		sc.bind(addr);
		
		key = sc.register(controller.getSelector(), SelectionKey.OP_ACCEPT);
		
		key.attach(this);
	}
	
	public void setFactory(Factory factory) {
		this.factory = factory;
	}
	
	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	@Override
	public void connect() { }

	@Override
	public void tryRead(ByteBuffer buffer) { }

	@Override
	public void tryWrite(ByteBuffer buffer) { }
}
