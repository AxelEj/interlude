package la2.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {
	private Properties properties;
	
	public Config(String file) throws IOException {
		properties = new Properties();
		
		BufferedReader reader = null;
		
		reader = new BufferedReader(new FileReader(file));
			
		properties.load(reader);
			
		reader.close();
	}
	
	public int getInt(String key) {
		return Integer.parseInt(properties.getProperty(key));
	}
	
	public int getInt(String key,int i) {
		return Integer.parseInt(properties.getProperty(key));
	}
	
	public String getString(String key) {
		return properties.getProperty(key);
	}
	
	public String getString(String key,String defaultValue) {
		return properties.getProperty(key,defaultValue);
	}

	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(properties.getProperty(key));
	}
}
