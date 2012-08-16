package la2.auth.task;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import task.Task;
import la2.auth.AuthClient;
import la2.auth.AuthClient.ClientState;
import la2.auth.net.client.RequestAuthLoginPacket;
import la2.auth.net.server.LoginFailPacket;
import la2.auth.net.server.LoginFailPacket.LoginFailReason;
import la2.auth.net.server.LoginSuccessPacket;
import la2.auth.AccountManager;

public class LoginTask extends Task<AuthClient> {
	private RequestAuthLoginPacket packet;
	
	public LoginTask(RequestAuthLoginPacket packet) {
		this.packet = packet;
	}
	

	@Override
	public void run() {
		packet.read();
		
		MessageDigest md5 = null;
		
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) { }
		
		md5.reset();
		
		md5.update(packet.getPassword().getBytes());
		
		String password = new BigInteger(1,md5.digest()).toString(16);
		
		if(client.getState().equals(ClientState.AUTH_GAME_GUARD)) {
			switch(AccountManager.getInstance().login(packet.getLogin(), password, client.getInetAddress().getHostName())) {
				case AccountManager.LOGIN_SUCCESS: {
					client.setState(ClientState.LOGIN);
					
					client.setLogin(packet.getLogin());
						
					client.send(new LoginSuccessPacket(client));
						
					break;
				}
				case AccountManager.ACCOUNT_NOT_FOUND:
				case AccountManager.ACCOUNT_WRONG_PASSWORD: {
					client.close(new LoginFailPacket(LoginFailReason.REASON_USER_OR_PASS_WRONG));
					
					break;
				}
				case AccountManager.ACCOUNT_IN_USE: {
					client.close(new LoginFailPacket(LoginFailReason.REASON_ACCOUNT_IN_USE));

					break;
				}
				case AccountManager.ACCOUNT_BLOCKED: {
					client.close(new LoginFailPacket(LoginFailReason.REASON_ACCOUNT_WAS_DENIED));

					break;
				}
			}
		} else 
			client.close(new LoginFailPacket(LoginFailReason.REASON_ACCESS_FAILED));
	}

}
