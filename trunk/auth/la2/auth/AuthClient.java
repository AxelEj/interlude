package la2.auth;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

import sql.QueryManager;
import task.TaskManager;

import la2.auth.net.client.*;
import la2.auth.sql.AuthDatabase;
import la2.auth.task.*;
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
		NULL,INIT,AUTH_GAME_GUARD,LOGIN,SELECT_SERVER,PLAY;
	}
	
	private ClientState state;
	
	private TaskManager<AuthClient> taskManager;
	
	private QueryManager<AuthClient> queryManager;
	
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
		
		queryManager = AuthDatabase.getInstance().create(this);
		
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
		if(!state.equals(ClientState.PLAY))
			taskManager.execute(new LogoutTask(login));

		setLogin(null);
	}
	
	@Override 
	public void onDisconnect() {
		if(!state.equals(ClientState.PLAY))
			taskManager.execute(new LogoutTask(login));
		
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
			case AuthGameGuardPacket.ID: {
				AuthGameGuardPacket packet = new AuthGameGuardPacket(buffer);
				
				taskManager.execute(new AuthGameGuardTask(packet));
				
				break;
			}
			case RequestAuthLoginPacket.ID: {
				RequestAuthLoginPacket packet = new RequestAuthLoginPacket(buffer,this);

				taskManager.execute(new LoginTask(packet));
				
				break;
			}
			case RequestServerListPacket.ID: {
				RequestServerListPacket packet = new RequestServerListPacket(buffer);
				
				taskManager.execute(new RequestServerListTask(packet));
				
				break;
			}
			case RequestServerLoginPacket.ID: {
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
	
	public QueryManager<AuthClient> getQueryManager() {
		return queryManager;
	}
}
