package la2.game;

import java.nio.ByteBuffer;

import task.TaskManager;

import la2.game.net.client.ProtocolVersionPacket;
import la2.game.net.client.auth.RequestAuthLoginPacket;
import la2.game.task.AuthTask;
import la2.game.task.GameExecutor;
import la2.game.task.InitTask;
import la2.game.task.LogoutTask;
import la2.util.GameCrypt;

import net.SelectionClient;
import net.io.Reader;
import net.io.SendablePacket;
import net.io.Writer;

public class GameClient 
	extends SelectionClient
	implements Reader,Writer
{
	private GameCrypt crypt;
	
	private String login;
	
	private TaskManager<GameClient> taskManager;
	
	public GameClient(byte[] key) {
		setReader(this);
		
		setWriter(this);
		
		crypt = new GameCrypt(key);
		
		taskManager = GameExecutor.getInstance().create(this);
	}

	@Override
	public void write(ByteBuffer buffer, SendablePacket packet) {
		int position = buffer.position();
		
		buffer.position(position + 2);
		
		packet.write();
		
		int size = buffer.position() - position - 2;  
		
		encrypt(buffer, position + 2, size);
		
		buffer.putShort(position,(short) (size+2));
		
		buffer.position(position + size + 2);
	}
	
	public void encrypt(ByteBuffer buffer,int offset,int size) {
		crypt.encrypt(buffer.array(), offset, size);
	}
	
	@Override
	public void read(ByteBuffer buffer) {
		int size = (buffer.getShort() & 0xFFFF) - 2;
		
		int position = buffer.position();
		
		decrypt(buffer, position, size);
		
		int id = buffer.get();
		
		switch(id) {
			case 0x00: {//ProtocolVersionPacket
				ProtocolVersionPacket packet = new ProtocolVersionPacket(buffer);
				
				taskManager.execute(new InitTask(packet));
				
				break;
			}
			case 0x08: {//RequestAuthLoginPacket
				RequestAuthLoginPacket packet = new RequestAuthLoginPacket(buffer);
				
				taskManager.execute(new AuthTask(packet));
				
				break;
			}
			case 0x09: {//Logout
				taskManager.execute(new LogoutTask(login));
				
				break;
			}
			case 0x0E: {//OpenCreationPacket STATIC
				
				break;
			}
			default: {
				System.out.println("packet id " + id);
			}
		}
		
		
		buffer.position(position+size);
	}

	public void decrypt(ByteBuffer buffer,int offset,int size) {
		crypt.decrypt(buffer.array(), offset, size);
	}

	public byte[] getCryptKey() {
		return crypt.getKey();
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}
