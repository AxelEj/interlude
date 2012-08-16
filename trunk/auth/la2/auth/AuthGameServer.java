package la2.auth;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import net.Factory;
import net.Filter;
import net.SelectionClient;
import net.SelectionServer;

public class AuthGameServer 
	extends SelectionServer 
	implements Factory,Filter
{
	private static AuthGameServer instance;
	
	private ArrayList<GameServer> list;

	public static AuthGameServer getInstance() {
		return instance;
	}
	
	public AuthGameServer(String host, int port) {
		super(host, port);

		setFactory(this);
		
		setFilter(this);
		
		list = AuthConfig.SERVER_LIST;
		
		instance = this;
	}
	
	@Override
	public boolean accept(SocketChannel sc) {
		return true;
	}
	
	public GameServer getServer(int id) {
		for(GameServer info : list)
			if(info.getServerId() == id)
				return info;
		return null;
	}
	
	public ArrayList<GameServer> getServerList() {
		return list;
	}
	
	
	@Override
	public SelectionClient create() {
		return new AuthGameClient();
	}
}
