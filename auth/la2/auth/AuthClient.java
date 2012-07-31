package la2.auth;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

import la2.auth.net.client.AuthGameGuardPacket;
import la2.auth.net.client.RequestAuthLoginPacket;
import la2.auth.net.client.RequestServerListPacket;
import la2.auth.net.client.RequestServerLoginPacket;
import la2.auth.task.AuthExecutor;
import la2.auth.task.AuthGameGuardTask;
import la2.auth.task.InitTask;
import la2.auth.task.LoginTask;
import la2.auth.task.RequestServerListTask;
import la2.auth.task.RequestServerLoginTask;
import la2.auth.util.AccountManager;
import la2.task.TaskManager;
import la2.util.LoginCrypt;
import la2.util.ScrambledKeyPair;
import la2.util.SessionKey;

import net.SelectionClient;
import net.io.Reader;
import net.io.SendablePacket;
import net.io.Writer;

public class AuthClient 
	extends SelectionClient
	implements Reader,Writer
{
	public static enum ClientState {
		NULL,INIT,AUTH_GAME_GUARD,LOGIN, SELECT_SERVER;
	}
	
	private ClientState state;
	
	private TaskManager<AuthClient> taskManager;
	
	private String login;
	
	private int session;
	
	private SessionKey key;
	
	private final ScrambledKeyPair scrambledKeyPair;
	
	private final byte[] blowfishKey;
	
	private LoginCrypt crypt;
	
	public AuthClient(Random random,ScrambledKeyPair scrambledKeyPair,byte[] blowfishKey) {
		setReader(this);
		
		setWriter(this);
		
		taskManager = AuthExecutor.getInstance().create(this);
		
		session = random.nextInt();
		
		key = new SessionKey(random);
		
		this.scrambledKeyPair = scrambledKeyPair;
		
		this.blowfishKey = blowfishKey;
		
		crypt = new LoginCrypt();
		
		crypt.setKey(blowfishKey, random);
	}
	
	@Override
	public void onAccept() {
		taskManager.execute(new InitTask());
	}
	
	@Override
	public void onClose() {
		setLogin(null);
	}
	
	@Override 
	public void onDisconnect() {
		AccountManager.getInstance().logout(login);
		
		setLogin(null);
	}
	
	@Override
	public void write(ByteBuffer buffer, SendablePacket packet) {
		buffer.position(2);
		
		packet.write();
		
		int size = buffer.position() - 2;
		
		buffer.position(2);

		encrypt(buffer,2,size);
		
		buffer.putShort(0,(short) buffer.position());
	}
	
	public boolean encrypt(ByteBuffer buffer,int offset,int size) {
		try {
			size = crypt.encrypt(buffer.array(), offset, size);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		buffer.position(offset + size);
		
		return true;
	}
	
	@Override
	public void read(ByteBuffer buffer) {
		int size =  (buffer.getShort() & 0xffff) - 2;
		
		int position = buffer.position();
		
		decrypt(buffer,position,size);
		
		int id = buffer.get();

		switch(id) {
			case 0x07: {
				AuthGameGuardPacket packet = new AuthGameGuardPacket(buffer);
				
				taskManager.execute(new AuthGameGuardTask(packet));
				
				break;
				
			}
			case 0x00: {
				RequestAuthLoginPacket packet = new RequestAuthLoginPacket(buffer,this);
				
				taskManager.execute(new LoginTask(packet));
				
				
				break;
			}
			case 0x05: {
				RequestServerListPacket packet = new RequestServerListPacket(buffer);
				
				taskManager.execute(new RequestServerListTask(packet));
				
				break;
			}
			case 0x02: {
				RequestServerLoginPacket packet = new RequestServerLoginPacket(buffer);
				
				taskManager.execute(new RequestServerLoginTask(packet));
				
				break;
			}
			default: {
				System.out.println("read packet id " + id);
			}
		}
		
		buffer.position(position + size);
	}

	public void decrypt(ByteBuffer buffer,int offset,int size) {
		try {
			crypt.decrypt(buffer.array(), offset, size);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ScrambledKeyPair getScrambledKeyPair() {
		return scrambledKeyPair;
	}

	public byte[] getBlowfishKey() {
		return blowfishKey;
	}

	public int getSession() { 
		return session;
	}

	public ClientState getState() {
		return state;
	}

	public void setState(ClientState state) {
		this.state = state;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public SessionKey getSessionKey() { 
		return key;
	}
}
