package br.com.educhainwallet.system.communication;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import org.apache.log4j.Logger;
import org.springframework.http.HttpMethod;

import com.google.gson.JsonObject;

import br.com.educhainwallet.model.Transaction;
import br.com.educhainwallet.setup.PropertiesManager;

public class MemPoolUtils {
	
	static Logger logger = Logger.getLogger(MemPoolUtils.class);
	
	private final static String URL_MEM_POOL = PropertiesManager.getInstance().getMemPoolHost()+"api/transaction/";
	
	private static HttpURLConnection openConnection(String urlName, String method) {
		HttpURLConnection con = null;
		try {
			URL url = new URL(urlName);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(method);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public static boolean sendTransaction(Transaction trans) throws InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException {
		HttpURLConnection con = openConnection(URL_MEM_POOL, HttpMethod.POST.toString());
		
		JsonObject transJson = new JsonObject();
		transJson.addProperty("sender", Base64.getEncoder().encodeToString(trans.getSender()));
		transJson.addProperty("receiver", Base64.getEncoder().encodeToString(trans.getReceiver()));
		transJson.addProperty("signature", Base64.getEncoder().encodeToString(trans.getSignature()));
		transJson.addProperty("amount", trans.getAmount());
		transJson.addProperty("fee", trans.getFee());
		transJson.addProperty("creationTime", trans.getCreationTime());
		transJson.addProperty("uniqueID", trans.getUniqueID());
		
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json");
		OutputStreamWriter out = null;
		try {
			out = new OutputStreamWriter(con.getOutputStream());
			out.write(transJson.toString());
	        out.close();
	        
	        int status = con.getResponseCode();
			if (status == 200) {
				logger.info("Removed a transaction from pool");
	            logger.debug("Transaction removed: "+trans);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}