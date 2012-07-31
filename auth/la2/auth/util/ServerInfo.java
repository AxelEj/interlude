package la2.auth.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import la2.auth.AuthGameClient;

public class ServerInfo {
	private int id;
	
	private byte[] ip;
	
	private int port;
	
	private int ageLimit;
	
	private boolean pvp;
	
	private int online;
	
	private int limit;
	
	private boolean test;
	
	private AuthGameClient server;
	
	public ServerInfo(int id,String host,int port) throws UnknownHostException {
		this.id = id;
		
		this.ip = InetAddress.getByName(host).getAddress();
		
		this.port = port;
	}
	
	public int getServerId() {
		return id;
	}
	
	public byte[] getAddress() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}

	public int getAgeLimit() {
		return ageLimit;
	}

	public void setAgeLimit(int ageLimit) {
		this.ageLimit = ageLimit;
	}
	
	public boolean isPvp() {
		return pvp;
	}
	
	public boolean isOnline() {
		return server != null ? ((SocketChannel)server.getChannel()).isConnected() : false;
	}
	
	public void setPvp(boolean pvp) {
		this.pvp = pvp;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	public AuthGameClient getServer() {
		return server;
	}

	public void setServer(AuthGameClient server) {
		this.server = server;
	}
}
