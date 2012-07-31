package test.net;


import java.nio.ByteBuffer;

import net.SelectionSocket;
import net.io.Reader;
import net.io.SendablePacket;
import net.io.Writer;

public class ClientSocket 
	extends SelectionSocket
	implements Reader,Writer
{

	public ClientSocket(String host, int port) {
		super(host, port,true);
		
		setReader(this);
		
		setWriter(this);
	}

	@Override
	public void onConnect() {
		System.out.println("ClientSocket onConnect");
	}
	
	@Override
	public void onClose() { 
		System.out.println("ClientSocket onClose");
	}
	
	@Override 
	public void onDisconnect() { 
		System.out.println("ClientSocket onDisconnect");
	}
	
	@Override
	public void write(ByteBuffer buffer, SendablePacket packet) {
		
	}

	@Override
	public void read(ByteBuffer buffer) {
		// TODO Auto-generated method stub
		
	}

}
