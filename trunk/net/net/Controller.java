package net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class Controller 
	extends Thread
{
	private ByteBuffer buffer = ByteBuffer.allocate(65536).order(ByteOrder.LITTLE_ENDIAN);
	
	private Selector selector;
	
	public Controller() throws IOException {
		selector = Selector.open();
	}
	
	public void register(Selection selection) throws IOException {
		selection.setController(this);
		
		selection.register(this);
	}

	@Override
	public void run() {
		while(true) {
			int num = 0;
			
			try {
				num = selector.selectNow();
			} catch (IOException e) {
				e.printStackTrace();
				
				return;
			}
			
			if(num > 0) {
				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				
				while(keys.hasNext()) {
					SelectionKey key = keys.next();
					
					Selection selection = (Selection) key.attachment();
					
					keys.remove();
					
					//System.out.println((key.isAcceptable()?"OP_ACCEPT ":"") + (key.isConnectable()?"OP_CONNECT ":"") + (key.isReadable()?"OP_READ ":"") + (key.isWritable()?"OP_WRITE ":""));
					
					switch(key.readyOps()) {
						case SelectionKey.OP_ACCEPT: {
							((SelectionServer)selection).accept();
							
							break;
						}
						case SelectionKey.OP_CONNECT: {
							selection.connect();
							
							break;
						}
						case SelectionKey.OP_WRITE: 
							selection.tryWrite(buffer);
							
							break;
						case SelectionKey.OP_READ | SelectionKey.OP_WRITE: 
							selection.tryWrite(buffer);
							
							selection.tryRead(buffer);
							
							break;
						case SelectionKey.OP_READ: 
							selection.tryRead(buffer);
							
							break;
					}
				}
			}
			
			try {
				Thread.sleep(10);//TODO config
			} catch (InterruptedException e) { }
		}
	}

	public Selector getSelector() {
		return selector;
	}
}
