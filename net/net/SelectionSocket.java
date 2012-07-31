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

public class SelectionSocket extends Selection {
	private Reader reader;
	
	private Writer writer;
	
	private boolean closes = false;
	
	private boolean reconnect = false;
	
	private ArrayList<SendablePacket> list = new ArrayList<SendablePacket>();
	
	public SelectionSocket(String host,int port) {
		addr = new InetSocketAddress(host,port);
	}
	
	public SelectionSocket(String host,int port,boolean reconnect) {
		addr = new InetSocketAddress(host,port);
		
		this.reconnect = reconnect;
	}
	@Override 
	public final void connect() {
		try {
			if(((SocketChannel) sc).finishConnect()) 
				onConnect();
		} catch (IOException e) {
			cancel();
			
			if(reconnect)
				reconnect();
		}
	}
	
	public final void close() {
		synchronized (list) {
			if(key.isValid()) {
				list.clear();
				
				closes = true;
				
				cancel();
				
				onClose();
			}
		}
	}
	
	public final void close(SendablePacket packet) {
		synchronized (list) {
			if(key.isValid() && !closes) {
				closes = true;
				
				list.clear();
				
				list.add(packet);
				
				key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
			}
		}
	}
	
	public void onConnect() { }
	
	public void onClose() { }
	
	public void onDisconnect() { }
	
	@Override
	public final void tryRead(ByteBuffer buffer) {
		synchronized (list) {
			synchronized(reader) {
				buffer.clear();
				
				int result = -2;
				
				try {
					result = ((SocketChannel)sc).read(buffer);
					
					buffer.flip();
				} catch(IOException e) {
					cancel();
					
					onDisconnect();//???
					
					reconnect();
					
					return;
				}
						
				if(result > 0) {
					for(int i = 0 ; i < 10 ; i++) {//TODO config
						if(buffer.hasRemaining()) {
							reader.read(buffer);
						} else
							break;
					}
					
					//TODO flood protection
				} else {
					cancel();
					
					onDisconnect();//???
					
					reconnect();
				}
			}
		}
	}
	
	public final void reconnect() {
		try {
			SocketChannel sc = SocketChannel.open();
			
			setChannel(sc);
			
			sc.configureBlocking(false);
			
			sc.connect(addr);
			
			key = sc.register(controller.getSelector(), SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
			
			key.attach(this);	
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public final void register(Controller controller) throws IOException {
		SocketChannel sc = SocketChannel.open();
		
		setChannel(sc);
		
		sc.configureBlocking(false);
		
		sc.connect(addr);
		
		key = sc.register(controller.getSelector(), SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
		
		key.attach(this);
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
	
	public final void setReader(Reader reader) {
		this.reader = reader;
	}
	
	public final void setWriter(Writer writer) {
		this.writer = writer;
	}

	
	@Override
	public final void tryWrite(ByteBuffer buffer) {
		synchronized (list) {
			synchronized (writer) {
				if(key.isValid()) {
					Iterator<SendablePacket> it = list.iterator();
					
					for(int i = 0 ; i < 10 && it.hasNext() ; i++) {//TODO config
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
					}
					
					if(list.size() == 0)
						key.interestOps(key.interestOps() ^ SelectionKey.OP_WRITE);
				}
			}
		}
	}
}
