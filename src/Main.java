import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import br.com.educhainwallet.setup.PropertiesManager;

public class Main {
	
	public static void main(String[] args){
		
		String privKeyPath = PropertiesManager.getInstance().getPrivKeyPath();
		FileInputStream keyfis;
		try {
			keyfis = new FileInputStream(privKeyPath);
			byte[] encKey = new byte[keyfis.available()];
			keyfis.read(encKey);
			keyfis.close();
			
			PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(encKey);

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privKey = keyFactory.generatePrivate(privKeySpec);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
