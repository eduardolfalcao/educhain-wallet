package br.com.educhainwallet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

import org.apache.log4j.Logger;

import br.com.educhainwallet.model.Transaction;

public class Signer {
	
	private static final Logger LOGGER = Logger.getLogger(Signer.class);
	
	public static byte[] sign(Transaction trans, PrivateKey privKey) {
		
		Signature sign;
		byte[] signature = null;
		try {
			sign = Signature.getInstance("SHA256withDSA");
			sign.initSign(privKey);
			
			byte[] transBytes = Signer.convertTransactionToByteArray(trans);
			sign.update(transBytes);
			
			signature = sign.sign();
			LOGGER.debug("Transaction "+trans+" had just been signed with privkey(hashCode) "+privKey.hashCode());
			LOGGER.info("Transaction had just been signed.");
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			e.printStackTrace();
		}
				
		return signature;
	}
	
	public static boolean verify(Transaction trans, byte[] signature) {
		try {
			Signature sign = Signature.getInstance("SHA256withDSA");
			sign.initVerify(trans.getSender());			
			sign.update(convertTransactionToByteArray(trans));
			boolean result = sign.verify(signature);
			if(result) {
				LOGGER.debug("Transaction "+trans+" had just been verified.");
				LOGGER.info("Transaction had just been verified.");
			} else {
				LOGGER.debug("Transaction "+trans+" couldn't be verified.");
				LOGGER.info("Transaction couldn't be verified.");
			}
			return result;
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			e.printStackTrace();
		}
		return false;		
	}
	
	private static byte[] convertTransactionToByteArray(Transaction trans) {
		byte[] transInBytes = null;
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
          out = new ObjectOutputStream(bos);   
          out.writeObject(trans);
          out.flush();
          transInBytes = bos.toByteArray();
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
          try {
            bos.close();
          } catch (IOException ex) {}
        }
        
        return transInBytes;
	}

}
