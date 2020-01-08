package br.com.educhainwallet.setup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {
	
	private static final String MEM_POOL_HOST = "mem_pool_host";
	private static final String WALLET_SENDER = "wallet_sender";
	private static final String WALLET_RECEIVER = "wallet_receiver";
	private static final String PRIV_KEY_EXTENSION = "priv_key_extension";
	private static final String PUB_KEY_EXTENSION = "pub_key_extension";
	
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
	
	public String getPrivKeyExtension() {
		return props.getProperty(PropertiesManager.PRIV_KEY_EXTENSION);
	}
	
	public String getPubKeyExtension() {
		return props.getProperty(PropertiesManager.PUB_KEY_EXTENSION);
	}
	
	public String getWalletSender() {
		return props.getProperty(PropertiesManager.WALLET_SENDER);
	}
	
	public String getWalletReceiver() {
		return props.getProperty(PropertiesManager.WALLET_RECEIVER);
	}
	
	public static void main(String[] args) {
		PropertiesManager prop = PropertiesManager.getInstance();
		System.out.println(prop.getMemPoolHost());
	}

}