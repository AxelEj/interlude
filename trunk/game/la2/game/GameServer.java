package la2.game;

import java.nio.channels.SocketChannel;
import java.util.Random;

import la2.util.Config;

import net.Factory;
import net.Filter;
import net.SelectionClient;
import net.SelectionServer;

public class GameServer 
	extends SelectionServer
	implements Factory,Filter
{
	private static Random random = new Random();
	
	private static GameServer instance;
	
	public static GameServer getInstance() {
		return instance;
	}

	private int id;
	
	private int ageLimit;
	
	private int limit;
	
	private int online;
	
	private boolean pvp;
	
	private boolean test;
	
	private byte[][] keys;
	
	public GameServer(Config config,String host,int port) {
		super(host,port);
		
		setFactory(this);
		
		setFilter(this);
		
		instance = this;
		
		id = config.getInt("SERVER_ID");
		
		ageLimit = config.getInt("AGE_LIMIT");
		
		limit = config.getInt("LIMIT");
		
		online = 0;
		
		pvp = config.getBoolean("PVP");
		
		test = config.getBoolean("TEST");
		
		keys = new byte[20][16];//TODO config size
		
		for (int i = 0; i < 20; i++) {//TODO conifg size
			for (int j = 0; j < keys[i].length; j++)
				 keys[i][j] = (byte) (random.nextDouble()*255);

			keys[i][8] = (byte) 0xc8;
			
			keys[i][9] = (byte) 0x27;
			
			keys[i][10] = (byte) 0x93;
			
			keys[i][11] = (byte) 0x01;
			
			keys[i][12] = (byte) 0xa1;
			
			keys[i][13] = (byte) 0x6c;
			
			keys[i][14] = (byte) 0x31;
			
			keys[i][15] = (byte) 0x97;
		}
	}

	@Override
	public boolean accept(SocketChannel sc) {
		return true;
	}

	@Override
	public SelectionClient create() {
		return new GameClient(keys[random.nextInt(20)]);
	}

	public int getServerId() {
		return id;
	}

	public int getAgeLimit() {
		return ageLimit;
	}

	public int getLimit() {
		return limit;
	}

	public int getOnline() {
		return online;
	}

	public boolean isPvp() {
		return pvp;
	}

	public boolean isTest() {
		return test;
	}

}
