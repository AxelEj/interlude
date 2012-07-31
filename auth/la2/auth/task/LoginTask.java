package la2.auth.task;

import la2.auth.AuthClient;
import la2.auth.AuthClient.ClientState;
import la2.auth.net.client.RequestAuthLoginPacket;
import la2.auth.net.server.LoginFailPacket;
import la2.auth.net.server.LoginFailPacket.LoginFailReason;
import la2.auth.net.server.LoginSuccessPacket;
import la2.auth.util.AccountManager;
import la2.task.Task;

public class LoginTask extends Task<AuthClient> {
	private RequestAuthLoginPacket packet;
	
	public LoginTask(RequestAuthLoginPacket packet) {
		this.packet = packet;
	}
	
	@Override
	public void run() {
		packet.read();
	
		if(client.getState().equals(ClientState.AUTH_GAME_GUARD)) {
			switch(AccountManager.getInstance().login(packet.getLogin(), packet.getPassword(), client.getInetAddress().getHostAddress())) {
				case 0: {
					client.setLogin(packet.getLogin());
					
					client.setState(ClientState.LOGIN);

					client.send(new LoginSuccessPacket(client));
					
					break;
				}
				case -4: {
					System.out.println("test -4");
					
					client.close(new LoginFailPacket(LoginFailReason.REASON_ACCOUNT_IN_USE));
					
					break;
				}
				case -2: {
					System.out.println("test -2");
					
					client.close(new LoginFailPacket(LoginFailReason.REASON_ACCOUNT_WAS_DENIED));
					
					break;
				}
				case -3:
				case -1: {
					System.out.println("test -1 -3");
					
					client.close(new LoginFailPacket(LoginFailReason.REASON_USER_OR_PASS_WRONG));
					
					break;
				}
			}
		} else
			client.close(new LoginFailPacket(LoginFailReason.REASON_ACCESS_FAILED));
		
	}

}
