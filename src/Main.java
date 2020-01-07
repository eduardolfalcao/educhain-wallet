import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import br.com.educhainwallet.Signer;
import br.com.educhainwallet.model.Transaction;
import br.com.educhainwallet.setup.PropertiesManager;

public class Main {

	public static void main(String[] args) {

		File privKeyFile = new File(getPrivKeyFilename());
		File pubKeyFile = new File(getPubKeyFilename());
		if (!privKeyFile.exists() || !pubKeyFile.exists()) {
			// generate and write keypair
			writeKeyPair();
		}
		
		PublicKey pubKey = readPubKey();
		PrivateKey privKey = readPrivKey();
		
		Transaction trans = new Transaction("Eduardo", "João", 35, 10, pubKey);
		byte[] signature = Signer.sign(trans, privKey);
		
		System.out.println(Signer.verify(trans, signature));
	
	}
	

	private static void writeKeyPair() {
		KeyPairGenerator keyPairGen;
		try {
			keyPairGen = KeyPairGenerator.getInstance("DSA");
			keyPairGen.initialize(2048);
			KeyPair pair = keyPairGen.generateKeyPair();
			
			System.out.println(keyPairGen.getAlgorithm());

			byte[] key = pair.getPublic().getEncoded();
			FileOutputStream keyfos = new FileOutputStream(getPubKeyFilename());
			keyfos.write(key);
			keyfos.close();

			key = pair.getPrivate().getEncoded();
			keyfos = new FileOutputStream(getPrivKeyFilename());
			keyfos.write(key);
			keyfos.close();
		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static PublicKey readPubKey() {
		byte [] pubKeyBytes = readKey(getPubKeyFilename());
		X509EncodedKeySpec specPub = new X509EncodedKeySpec(pubKeyBytes);
		KeyFactory kf;
		PublicKey pubKey = null;
		try {
			kf = KeyFactory.getInstance("DSA");
			pubKey = kf.generatePublic(specPub);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pubKey;
		
	}
	
	private static PrivateKey readPrivKey() {
		byte [] privKeyBytes = readKey(getPrivKeyFilename());
		PKCS8EncodedKeySpec specPriv = new PKCS8EncodedKeySpec(privKeyBytes);
		KeyFactory kf;
		PrivateKey privKey = null;
		try {
			kf = KeyFactory.getInstance("DSA");
			privKey = kf.generatePrivate(specPriv);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return privKey;
		
	}

	private static byte[] readKey(String filename) {
		File f = new File(filename);
		FileInputStream fis;
		byte[] keyBytes = null;
		try {
			fis = new FileInputStream(f);
			DataInputStream dis = new DataInputStream(fis);
			keyBytes = new byte[(int) f.length()];
			dis.readFully(keyBytes);
			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return keyBytes;
	}

	private static String getPrivKeyFilename() {
		return PropertiesManager.getInstance().getWalletOwner() + PropertiesManager.getInstance().getPrivKeyExtension();
	}

	private static String getPubKeyFilename() {
		return PropertiesManager.getInstance().getWalletOwner() + PropertiesManager.getInstance().getPubKeyExtension();
	}

}
