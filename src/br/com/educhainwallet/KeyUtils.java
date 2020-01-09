package br.com.educhainwallet;

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

import org.apache.log4j.Logger;

import br.com.educhainwallet.setup.PropertiesManager;

public class KeyUtils {
	
	private static final Logger LOGGER = Logger.getLogger(KeyUtils.class);
	private static final String KEYGEN_ALGORITHM = PropertiesManager.getInstance().getKeyGenAlgorithm();
	
	public static void writeKeyPair(String walletOwner) {
		KeyPairGenerator keyPairGen;
		try {
			keyPairGen = KeyPairGenerator.getInstance(KEYGEN_ALGORITHM);
			keyPairGen.initialize(2048);
			KeyPair pair = keyPairGen.generateKeyPair();
			
			LOGGER.debug("Algorithm used for key generation: "+keyPairGen.getAlgorithm());			

			byte[] key = pair.getPublic().getEncoded();
			FileOutputStream keyfos = new FileOutputStream(getPubKeyFilename(walletOwner));
			keyfos.write(key);
			keyfos.close();
			LOGGER.info("Public key for "+walletOwner+" had just been written.");

			key = pair.getPrivate().getEncoded();
			keyfos = new FileOutputStream(getPrivKeyFilename(walletOwner));
			keyfos.write(key);
			keyfos.close();
			LOGGER.info("Private key for "+walletOwner+" had just been written.");
		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PublicKey readPubKey(String walletOwner) {
		byte [] pubKeyBytes = readKey(getPubKeyFilename(walletOwner));
		X509EncodedKeySpec specPub = new X509EncodedKeySpec(pubKeyBytes);
		KeyFactory kf;
		PublicKey pubKey = null;
		try {
			kf = KeyFactory.getInstance(KEYGEN_ALGORITHM);
			pubKey = kf.generatePublic(specPub);
			LOGGER.info("Public key for "+walletOwner+" had just been read.");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return pubKey;
		
	}
	
	public static PrivateKey readPrivKey(String walletOwner) {
		byte [] privKeyBytes = readKey(getPrivKeyFilename(walletOwner));
		PKCS8EncodedKeySpec specPriv = new PKCS8EncodedKeySpec(privKeyBytes);
		KeyFactory kf;
		PrivateKey privKey = null;
		try {
			kf = KeyFactory.getInstance(KEYGEN_ALGORITHM);
			privKey = kf.generatePrivate(specPriv);
			LOGGER.info("Private key for "+walletOwner+" had just been read.");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
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

	public static String getPrivKeyFilename(String walletOwner) {
		return walletOwner + PropertiesManager.getInstance().getPrivKeyExtension();
	}

	public static String getPubKeyFilename(String walletOwner) {
		return walletOwner + PropertiesManager.getInstance().getPubKeyExtension();
	}

}
