package test.net;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.SelectionClient;
import net.io.Reader;
import net.io.SendablePacket;
import net.io.Writer;

public class Client 
	extends SelectionClient
	implements Reader,Writer
{
	public Client() {
		setReader(this);
		
		setWriter(this);
	}
	
	@Override
	public void write(ByteBuffer buffer, SendablePacket packet) {
		
	}

	@Override
	public void read(ByteBuffer buffer) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onAccept() {
		System.out.println("Client onAccept");
	}
	
	@Override
	public void onClose() { 
		System.out.println("Client onClose");
	}
	
	@Override 
	public void onDisconnect() { 
		System.out.println("Client onDisconnect");
	}
}
