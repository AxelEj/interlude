package la2.game;

import java.io.IOException;

import data.DataType;
import data.io.SerializeReader;

public class GameConfig {
	public static String GAME_HOST;
	
	public static int GAME_PORT;
	
	public static String AUTH_HOST;
	
	public static int AUTH_PORT;
	
	public static String SQL_DRIVER;
	
	public static String SQL_URL;
	
	public static int SERVER_ID;
	
	public static int AGE_LIMIT;
	
	public static int LIMIT;
	
	public static boolean PVP;
	
	public static boolean TEST;
	
	public static int TASK_THREADS_COUNT;

	public static int TASK_SCHEDULER_THREADS_COUNT;
	
	public GameConfig() {
		SerializeReader reader;
		
		try {
			reader = new SerializeReader("game.config.dat");
			
			reader.read();
			
			DataType data = reader.getDataTable().get("game");
			
			GAME_HOST = data.getString("host");
			
			GAME_PORT = data.getInt("port");
			
			data = reader.getDataTable().get("auth");
			
			AUTH_HOST = data.getString("host");
			
			AUTH_PORT = data.getInt("port");
			
			data = reader.getDataTable().get("sql");
			
			SQL_DRIVER = data.getString("driver");
			
			SQL_URL = data.getString("url");
			
			SERVER_ID =  reader.getDataTable().get("server_id").getInt();
			
			AGE_LIMIT =  reader.getDataTable().get("age_limit").getInt();
			
			LIMIT =  reader.getDataTable().get("limit").getInt();
			
			PVP =  reader.getDataTable().get("pvp").getBoolean();
			
			TEST =  reader.getDataTable().get("test").getBoolean();
			
			TASK_THREADS_COUNT = reader.getDataTable().get("task_threads_count").getInt();
			
			TASK_SCHEDULER_THREADS_COUNT = reader.getDataTable().get("task_scheduler_threads_count").getInt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
