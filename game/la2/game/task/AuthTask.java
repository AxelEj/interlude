package la2.game.task;

import task.Task;
import la2.game.AuthClient;
import la2.game.GameClient;
import la2.game.net.client.auth.RequestAuthLoginPacket;
import la2.game.net.server.CharacterListPacket;
import la2.game.net.server.auth.LoginFailPacket;
import la2.game.net.server.auth.LoginSuccessPacket;
import la2.util.SessionKey;

public class AuthTask extends Task<GameClient> {
	private RequestAuthLoginPacket packet;
	
	public AuthTask(RequestAuthLoginPacket packet) {
		this.packet = packet;
	}
	
	@Override
	public void run() {
		SessionKey key;
		
		String login;
		
		synchronized (AuthClient.getInstance().getWaitList()) {
			login = packet.getLogin();
			
			key = AuthClient.getInstance().getWaitList().get(login);
			
			AuthClient.getInstance().getWaitList().remove(login);
			
			if(key != null && packet.getSessionKey().equals(key)) {
				client.setLogin(login);
				
				AuthClient.getInstance().send(new LoginSuccessPacket(login));
				
				client.send(new CharacterListPacket(client));
			} else {
				AuthClient.getInstance().send(new LoginFailPacket(login));
				
				client.close();
			}
		}
		

	}
	
}
