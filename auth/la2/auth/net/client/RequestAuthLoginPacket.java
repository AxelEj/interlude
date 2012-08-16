package la2.auth.net.client;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

import javax.crypto.Cipher;

import net.io.RecvablePacket;

import la2.auth.AuthClient;


public class RequestAuthLoginPacket extends RecvablePacket {
	public static final int ID = 0x00;

	private PrivateKey key;
	
	private byte[] _raw = new byte[128];

	private String login;
	
	private String password;
	
	private int ncotp;

	public RequestAuthLoginPacket(ByteBuffer buffer,AuthClient client) {
		super(buffer);
		
		readB(_raw);
		
		key = client.getScrambledKeyPair()._pair.getPrivate();
	}
	
	public void read() {
		byte[] decrypted = null;
		
		try {
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
			rsaCipher.init(Cipher.DECRYPT_MODE, key);
			decrypted = rsaCipher.doFinal(_raw, 0x00, 0x80 );
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return;
		}

		login = new String(decrypted, 0x5E, 14 ).trim().toLowerCase();
		
		password = new String(decrypted, 0x6C, 16).trim();
		
		ncotp = decrypted[0x7c];
		
		ncotp |= decrypted[0x7d] << 8;
		
		ncotp |= decrypted[0x7e] << 16;
		
		ncotp |= decrypted[0x7f] << 24;
	}
	
	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public int getOneTimePassword() {
		return ncotp;
	}
}
