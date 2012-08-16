package la2.auth;

import java.io.IOException;
import java.util.ArrayList;

import data.DataType;
import data.io.SerializeReader;

public class AuthConfig {
	public static String AUTH_HOST;
	
	public static int AUTH_PORT;
	
	public static String GAME_HOST;
	
	public static int GAME_PORT;
	
	public static String SQL_DRIVER;
	
	public static String SQL_URL;

	public static ArrayList<GameServer> SERVER_LIST  = new ArrayList<GameServer>();
	
	public static int TASK_THREADS_COUNT;
	
	public static int TASK_SCHEDULER_THREADS_COUNT;
	
	public AuthConfig() {
		SerializeReader reader;
		
		try {
			reader = new SerializeReader("auth.config.dat");
			
			reader.read();
			
			DataType addr = reader.getDataTable().get("auth");

			AUTH_HOST = addr.getString("host");
			
			AUTH_PORT = addr.getInt("port");
			
			addr = reader.getDataTable().get("game");
			
			GAME_HOST = addr.getString("host");
			
			GAME_PORT = addr.getInt("port");
			
			DataType sql = reader.getDataTable().get("sql");
			
			SQL_DRIVER = sql.getString("driver");
			
			SQL_URL = sql.getString("url");
			
			ArrayList<DataType> server_list = reader.getDataTable().get("server_list").getList();
			
			for(DataType server : server_list)
				SERVER_LIST.add(new GameServer(server.getInt("id"),server.get("addr").getString("host"),server.get("addr").getInt("port")));

			TASK_THREADS_COUNT = reader.getDataTable().get("task_threads_count").getInt();
		
			TASK_SCHEDULER_THREADS_COUNT = reader.getDataTable().get("task_scheduler_threads_count").getInt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
