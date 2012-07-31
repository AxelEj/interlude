package la2.auth;

import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import la2.auth.util.ServerInfo;
import la2.sql.DatabaseManager;

import net.Factory;
import net.Filter;
import net.SelectionClient;
import net.SelectionServer;

public class AuthGameServer 
	extends SelectionServer 
	implements Factory,Filter
{
	private static final String SERVER_LIST = "SELECT id,host,port FROM server";
	
	private static AuthGameServer instance;
	
	private ArrayList<ServerInfo> list = new ArrayList<ServerInfo>();

	public static AuthGameServer getInstance() {
		return instance;
	}
	
	public AuthGameServer(String host, int port) {
		super(host, port);

		setFactory(this);
		
		setFilter(this);
		
		//load server info from db
		try {
			Connection con = DatabaseManager.getInstance().getConnection();
			
			Statement st = con.createStatement();
			
			ResultSet result = st.executeQuery(SERVER_LIST);
			
			
			while(result.next()) 
				list.add(new ServerInfo(result.getInt(1),result.getString(2),result.getInt(3)));
	
			st.close();
			
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		instance = this;
	}
	
	@Override
	public boolean accept(SocketChannel sc) {
		return true;
	}
	
	public ServerInfo getServer(int id) {
		for(ServerInfo info : list)
			if(info.getServerId() == id)
				return info;
		return null;
	}
	
	public ArrayList<ServerInfo> getServerList() {
		return list;
	}
	
	
	@Override
	public SelectionClient create() {
		return new AuthGameClient();
	}
	

}
