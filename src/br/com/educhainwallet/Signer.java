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

import br.com.educhainwallet.model.Transaction;

public class Signer {
	
	public static byte[] sign(Transaction trans, PrivateKey privKey) {
		
		Signature sign;
		byte[] signature = null;
		try {
			sign = Signature.getInstance("SHA256withDSA");
			sign.initSign(privKey);
			
			byte[] transBytes = Signer.convertTransactionToByteArray(trans);
			sign.update(transBytes);
			
			signature = sign.sign();
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			e.printStackTrace();
		}
				
		return signature;
	}
	
	public static boolean verify(Transaction trans, byte[] signature) {
		try {
			Signature sign = Signature.getInstance("SHA256withDSA");
			sign.initVerify(trans.getPubKey());			
			sign.update(convertTransactionToByteArray(trans));			
			return sign.verify(signature);
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
