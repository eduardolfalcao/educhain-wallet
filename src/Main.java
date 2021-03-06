import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

import br.com.educhainwallet.KeyUtils;
import br.com.educhainwallet.Signer;
import br.com.educhainwallet.config.PropertiesManager;
import br.com.educhainwallet.model.Transaction;
import br.com.educhainwallet.system.communication.MemPoolUtils;

public class Main {

	public static void main(String[] args) {

		File privKeySenderFile = new File(
				KeyUtils.getPrivKeyFilename(PropertiesManager.getInstance().getWalletSender()));
		File pubKeySenderFile = new File(KeyUtils.getPubKeyFilename(PropertiesManager.getInstance().getWalletSender()));
		if (!privKeySenderFile.exists() || !pubKeySenderFile.exists()) {
			// generate and write keypair
			KeyUtils.writeKeyPair(PropertiesManager.getInstance().getWalletSender());
		}

		File pubKeyReceiverFile = new File(
				KeyUtils.getPubKeyFilename(PropertiesManager.getInstance().getWalletReceiver()));
		if (!pubKeyReceiverFile.exists()) {
			KeyUtils.writeKeyPair(PropertiesManager.getInstance().getWalletReceiver());
		}

		PublicKey pubKeySender = KeyUtils.readPubKey(PropertiesManager.getInstance().getWalletSender());
		PrivateKey privKeySender = KeyUtils.readPrivKey(PropertiesManager.getInstance().getWalletSender());
		PublicKey pubKeyReceiver = KeyUtils.readPubKey(PropertiesManager.getInstance().getWalletReceiver());
				
		Transaction trans = new Transaction(pubKeySender.getEncoded(), pubKeyReceiver.getEncoded(), null,
				PropertiesManager.getInstance().getTransAmount(), PropertiesManager.getInstance().getTransFee());
				
		if(signAndVerify(trans, privKeySender))
			sendTransaction(trans, privKeySender);
	}

	public static void sendTransaction(Transaction trans, PrivateKey privKeySender) {

		byte[] signature = Signer.sign(trans, privKeySender);
		trans.setSignature(signature);
		try {
			MemPoolUtils.sendTransaction(trans);
		} catch (InvalidKeySpecException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public static boolean signAndVerify(Transaction trans, PrivateKey privKeySender) {
		byte[] signature = Signer.sign(trans, privKeySender);
		trans.setSignature(signature);

		// this line would fail the verification
		//trans.setSender(trans.getReceiver());

		return Signer.verify(trans);
	}

}
