package la2.auth;

import java.nio.ByteBuffer;
import java.util.HashMap;

import la2.auth.net.client.game.InitServerPacket;
import la2.auth.net.client.game.LoginFailPacket;
import la2.auth.net.client.game.LoginSuccessPacket;
import la2.auth.net.client.game.LogoutPacket;
import la2.auth.net.client.game.WaitingLoginPacket;
import la2.auth.task.AuthExecutor;
import la2.auth.task.game.InitServerTask;
import la2.auth.task.game.LoginFailTask;
import la2.auth.task.game.LoginSuccessTask;
import la2.auth.task.game.LogoutTask;
import la2.auth.task.game.WaitingLoginTask;
import la2.auth.util.ServerInfo;
import la2.task.TaskManager;

import net.SelectionClient;
import net.io.Reader;
import net.io.SendablePacket;
import net.io.Writer;

/**
 * TODO synchronized loginList
 * if lost connection and then reconnect synchronize login list
 * 
 * @author сапер
 *
 */
public class AuthGameClient 
	extends SelectionClient
	implements Reader,Writer
{ 
	private ServerInfo server;
	
	private TaskManager<AuthGameClient> taskManager;
	
	private HashMap<String,AuthClient>  waitingList = new HashMap<String, AuthClient>();
	
	//private ArrayList<String> loginList = new ArrayList<String>();
	
	public AuthGameClient() {
		setReader(this);
		
		setWriter(this);
		
		taskManager = AuthExecutor.getInstance().create(this);
	}
	
	@Override
	public void onAccept() { }
	
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
		int id = buffer.get();
		
		switch(id) {
			case 0x00: {//InitServerPacket
				InitServerPacket packet = new InitServerPacket(buffer);
					
				taskManager.execute(new InitServerTask(packet));
				
				break;
			}
			case 0x01: {//WaitingLogin
				WaitingLoginPacket packet = new WaitingLoginPacket(buffer);
				
				taskManager.execute(new WaitingLoginTask(packet));
				
				break;
			}
			
			case 0x02: {//LoginSuccessPacket
				LoginSuccessPacket packet = new LoginSuccessPacket(buffer);
				
				taskManager.execute(new LoginSuccessTask(packet));
				
				break;
			}
			case 0x03: {//LoginFailPacket
				LoginFailPacket packet = new LoginFailPacket(buffer);
				
				taskManager.execute(new LoginFailTask(packet));
				
				break;
			}
			case 0x04: {//LogoutPacket
				LogoutPacket packet = new LogoutPacket(buffer);
				
				taskManager.execute(new LogoutTask(packet));
				
				break;
			}
		}
	}

	public ServerInfo getServer() {
		return server;
	}

	public void setServer(ServerInfo server) {
		this.server = server;
	}
	
	public HashMap<String,AuthClient> getWaitingList() {
		return waitingList;
	}

}
