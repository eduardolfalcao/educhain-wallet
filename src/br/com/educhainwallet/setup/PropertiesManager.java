package br.com.educhainwallet.setup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {
	
	private static final String MEM_POOL_HOST = "mem_pool_host";
	private static final String PRIV_KEY_PATH = "priv_key_path";
	private static final String PUB_KEY_PATH = "pub_key_path";
	
	private static PropertiesManager instance = new PropertiesManager();
	private Properties props;
	
	private PropertiesManager() {
		this.props = new Properties();
		try {
			props.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static PropertiesManager getInstance() {
		return instance;
	}
	
	public String getMemPoolHost() {
		return props.getProperty(PropertiesManager.MEM_POOL_HOST);
	}
	
	public String getPrivKeyPath() {
		return props.getProperty(PropertiesManager.PRIV_KEY_PATH);
	}
	
	public String getPubKeyPath() {
		return props.getProperty(PropertiesManager.PUB_KEY_PATH);
	}
	
	public static void main(String[] args) {
		PropertiesManager prop = PropertiesManager.getInstance();
		System.out.println(prop.getMemPoolHost());
	}

}