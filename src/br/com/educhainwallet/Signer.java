package br.com.educhainwallet;

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
			sign.update(trans.toString().getBytes());

			signature = sign.sign();
			LOGGER.debug("Transaction " + trans + " had just been signed with privkey(hashCode) " + privKey.hashCode());
			LOGGER.info("Transaction had just been signed.");
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			e.printStackTrace();
		}

		return signature;
	}

	public static boolean verify(Transaction trans) {
		try {
			Signature sign = Signature.getInstance("SHA256withDSA");
			sign.initVerify(trans.getPubKey(trans.getSender()));
			sign.update(trans.toString().getBytes());
			boolean result = sign.verify(trans.getSignature());
			if (result) {
				LOGGER.debug("Transaction " + trans + " had just been verified.");
				LOGGER.info("Transaction had just been verified.");
			} else {
				LOGGER.debug("Transaction " + trans + " couldn't be verified.");
				LOGGER.info("Transaction couldn't be verified.");
			}
			return result;
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			e.printStackTrace();
		}
		return false;
	}

}