import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

import br.com.educhainwallet.setup.PropertiesManager;

public class Main {
	
	public static void main(String[] args){
		
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
			keyPairGen.initialize(2048);
			KeyPair pair = keyPairGen.generateKeyPair();
			
			PrivateKey privKey = pair.getPrivate();
			
			Signature sign = Signature.getInstance("SHA256withDSA");
			sign.initSign(privKey);
			
			byte[] bytes = "Hello how are you".getBytes();
			sign.update(bytes);
			
			byte[] signature = sign.sign();
			
			sign.initVerify(pair.getPublic());
			sign.update(bytes);
			
			boolean bool = sign.verify(signature);
			System.out.println(bool);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
