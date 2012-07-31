package la2.util;

import java.io.IOException;
import java.util.Random;

public class LoginCrypt
{
	private static final byte[] STATIC_BLOWFISH_KEY =
	{
		(byte) 0x6b, (byte) 0x60, (byte) 0xcb, (byte) 0x5b,
		(byte) 0x82, (byte) 0xce, (byte) 0x90, (byte) 0xb1,
		(byte) 0xcc, (byte) 0x2b, (byte) 0x6c, (byte) 0x55,
		(byte) 0x6c, (byte) 0x6c, (byte) 0x6c, (byte) 0x6c
	};

	private NewCrypt _staticCrypt = new NewCrypt(STATIC_BLOWFISH_KEY);
	
    private NewCrypt _crypt;
    
    private boolean _static = true;
    
    private Random random;
 
    public void setKey(byte[] key,Random random) {
        _crypt = new NewCrypt(key);
        
        this.random = random;
    }

    public boolean decrypt(byte[] raw, final int offset, final int size) throws IOException
    {
    	_crypt.decrypt(raw, offset, size);
    	return NewCrypt.verifyChecksum(raw, offset, size);
    }

    public int encrypt(byte[] raw, final int offset, int size) throws IOException
    {
    	// reserve checksum
    	size += 4;


    	if (_static)
    	{
    		// reserve for XOR "key"
    		size += 4;

    		// padding
        	size += 8 - size%8;
    		NewCrypt.encXORPass(raw, offset, size, random.nextInt());
    		_staticCrypt.crypt(raw, offset, size);

    		_static = false;
    	}
    	else
    	{
    		// padding
        	size += 8 - size%8;
    		NewCrypt.appendChecksum(raw, offset, size);
    		_crypt.crypt(raw, offset, size);
    	}
    	return size;
    }
}
