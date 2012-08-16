package la2.auth;


import java.nio.channels.SocketChannel;
import java.security.GeneralSecurityException;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Random;

import la2.util.ScrambledKeyPair;



import net.Factory;
import net.Filter;
import net.SelectionClient;
import net.SelectionServer;

public class AuthServer 
	extends SelectionServer
	implements Factory,Filter
{
	private Random random = new Random();
	
	private byte[][] keys;
	
	private ScrambledKeyPair[] scrambledKeys;
	
	public AuthServer(String host, int port) throws GeneralSecurityException {
		super(host, port);
		
		setFactory(this);
		
		setFilter(this);
		
		keys = new byte[10][16];
		
		scrambledKeys = new ScrambledKeyPair[10];

		KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
	
		keygen.initialize(new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4));

		for (int i = 0; i < 10; i++)
			scrambledKeys[i] = new ScrambledKeyPair(keygen.generateKeyPair());
		
		for(int i = 0 ; i < 10 ; i++) 
			for (int n = 0 ; n < keys[i].length; n++)
				keys[i][n] = (byte) (random.nextDouble()*255 + 1);
	}
	
	@Override
	public boolean accept(SocketChannel arg0) {
		return true;
	}
	
	@Override
	public SelectionClient create() {
		return new AuthClient(random,scrambledKeys[(int) (Math.random()*10)],keys[(int) (Math.random()*10)]);
	}

}
