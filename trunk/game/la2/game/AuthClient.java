package la2.game;

import java.nio.ByteBuffer;
import java.util.HashMap;


import la2.game.net.client.auth.*;
import la2.game.net.server.auth.*;
import la2.util.SessionKey;

import net.SelectionSocket;
import net.io.Reader;
import net.io.SendablePacket;
import net.io.Writer;


public class AuthClient 
	extends SelectionSocket
	implements Reader,Writer
{
	private static AuthClient instance;
	
	public static AuthClient getInstance() {
		return instance;
	}
	
	private HashMap<String, SessionKey> list = new HashMap<String,SessionKey>();
	
	public AuthClient(String host,int port) {
		super(host,port,true);
		
		setReader(this);
		
		setWriter(this);
		
		instance = this;
	}
	
	@Override
	public void onConnect() {
		send(new InitServerPacket(GameServer.getInstance()));
	}
	
	@Override
	public void onClose() { }
	
	@Override
	public void onDisconnect() { }
	
	@Override
	public void write(ByteBuffer buffer, SendablePacket packet) {
		packet.write();
	}

	@Override
	public void read(ByteBuffer buffer) {
		int id = buffer.get() & 0xff;
		
		switch(id) {
			case 0x00: {//LoginPacket
				LoginPacket packet = new LoginPacket(buffer);
				
				synchronized (list) {
					list.put(packet.getLogin(), packet.getKey());
				}
				
				send(new WaitingLoginPacket(packet.getLogin()));
				
				break;
			}
		}
	}

	public HashMap<String, SessionKey> getWaitList() {
		// TODO Auto-generated method stub
		return list;
	}

}
