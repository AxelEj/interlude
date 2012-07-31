package la2.auth.net.server;

import net.io.SendablePacket;
import la2.auth.AuthClient;

public class InitPacket extends SendablePacket {
	private int session;

	private byte[] publicKey;
	
	private byte[] blowfishKey;

	public InitPacket(AuthClient client) {
		this(client.getScrambledKeyPair()._scrambledModulus,client.getBlowfishKey(),client.getSession());
	}
	
    public InitPacket(byte[] publicKey, byte[] blowfishKey, int session) {
    	this.session = session;
    	
    	this.publicKey = publicKey;
    	
    	this.blowfishKey = blowfishKey;
    }
    
    @Override
    public void write() {
    	writeB(0x00);
    	
    	writeDW(session); // session id
    	writeDW(0x0000c621); // protocol revision
    	
    	writeB(publicKey); // RSA Public Key
    	
    	writeDW(0x29DD954E);
    	writeDW(0x77C39CFC);
    	writeDW(0x97ADB620);
    	writeDW(0x07BDE0F7);
    	
    	writeB(blowfishKey); // BlowFish key
        writeB(0x00);
    }
}
