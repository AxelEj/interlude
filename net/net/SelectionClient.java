package net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

import net.io.Reader;
import net.io.SendablePacket;
import net.io.Writer;

public class SelectionClient extends Selection{
	private Reader reader;
	
	private Writer writer;
	
	private boolean closes = false;
	
	private ArrayList<SendablePacket> list = new ArrayList<SendablePacket>();
	
	public final void close() {
		synchronized (list) {
			if(key.isValid()) {
				list.clear();
				
				cancel();
				
				onClose();
			}
		}
	}

	public final void close(SendablePacket packet) {
		synchronized (list) {
			if(key.isValid() && !closes) {
				list.clear();
					
				list.add(packet);
				
				key.interestOps(SelectionKey.OP_WRITE);
				
				closes = true;
			}
		}
	}
	
	public void onAccept() { }
	
	public void onClose() { }
	
	public void onDisconnect() { }
	
	@Override
	public void register(Controller controller) throws IOException {
		sc.configureBlocking(false);
		
		key = sc.register(controller.getSelector(), SelectionKey.OP_READ);
		
		key.attach(this);
		
		addr = (InetSocketAddress) ((SocketChannel)sc).getRemoteAddress();
		
		onAccept();
	}

	@Override
	public void tryRead(ByteBuffer buffer) {
		synchronized (list) {
			synchronized (reader) {
				if(key.isValid() && !closes) {
					buffer.clear();
					
					int result = -2;
						
					try {
						result = ((SocketChannel)sc).read(buffer);
						
						buffer.flip();
					} catch (IOException e) {
						cancel();
						
						onDisconnect();
						
						return;
					}
					
					if(result > 0) {
						for(int i = 0 ; i < 10 ; i++) {//TODO config
							if(buffer.hasRemaining()) {//TODO into thread
								if(key.isValid())
									reader.read(buffer);
							} else 
								break;
						}		
						//TODO flood prot
					} else {//TODO переполнение
						cancel();
						
						onDisconnect();
					}
				}
			}
		}
	}
	
	public void send(SendablePacket packet) {
		synchronized (list) {
			if(!closes && key.isValid()) {
				if(list.size() == 0) 
					key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
				
				list.add(packet);
			}
		}
	}
	
	public void setReader(Reader reader) {
		synchronized (reader) {
			this.reader = reader;
		}
	}
	
	public void setWriter(Writer writer) {
		synchronized (writer) {
			this.writer = writer;
		}
	}
	
	@Override
	public void tryWrite(ByteBuffer buffer) {
		synchronized (list) {
			synchronized (writer) {
				if(key.isValid()) {
					Iterator<SendablePacket> it = list.iterator();
					
					for(int i = 0 ; i < 10 && it.hasNext(); i++) {//TODO config
						SendablePacket packet = it.next();
						
						it.remove();
						
						buffer.clear();
						
						packet.setBuffer(buffer);
						
						writer.write(buffer, packet);
						
						buffer.flip();
						
						try {
							while(buffer.hasRemaining())
								((SocketChannel)sc).write(buffer);
						} catch (IOException e) {
							cancel();
							
							onDisconnect();
							
							return;
						}
					}
					
					if(closes) {
						cancel();
						
						onClose();
						
						return;
					} else if(list.size() == 0)
						key.interestOps(key.interestOps() ^ SelectionKey.OP_WRITE);
				}
			}
		}
	}

	@Override
	public void connect() { }
}
