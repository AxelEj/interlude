package la2.util;

import java.util.Random;

public class SessionKey {
	private final int key1;
	
	private final int key2;
	
	private final int key3;
	
	private final int key4;
	
	public SessionKey(Random random) {
		key1 = random.nextInt();
		
		key2 = random.nextInt();
		
		key3 = random.nextInt();
		
		key4 = random.nextInt();
	}
	
	public SessionKey(int key1, int key2, int key3, int key4) {
		this.key1 = key1;
		
		this.key2 = key2;
		
		this.key3 = key3;
		
		this.key4 = key4;
	}

	public int getKey1() {
		return key1;
	}
	
	public int getKey2() {
		return key2;
	}
	
	public int getKey3() {
		return key3;
	}
	
	public int getKey4() {
		return key4;
	}
	
	public boolean equals(SessionKey key) {
		return key1 == key.key1 && key2 == key.key2 && key3 == key.key3 && key4 == key.key4;
	}
}
