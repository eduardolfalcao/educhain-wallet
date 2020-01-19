import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

import br.com.educhainwallet.KeyUtils;
import br.com.educhainwallet.Signer;
import br.com.educhainwallet.model.Transaction;
import br.com.educhainwallet.setup.PropertiesManager;

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

		signAndVerifyExample(pubKeySender, privKeySender, pubKeyReceiver);
	}

//	public static void sendTransaction(PublicKey pubKeySender, PrivateKey privKeySender, PublicKey pubKeyReceiver,
//			Transaction trans) {
//
//		trans.setSender(pubKeySender.getEncoded());
//		trans.setReceiver(pubKeyReceiver.getEncoded());
//		byte[] signature = Signer.sign(trans, privKeySender);
//		trans.setSignature(signature);
//
//	}

	public static void signAndVerifyExample(PublicKey pubKeySender, PrivateKey privKeySender, PublicKey pubKeyReceiver) {
		Transaction trans = new Transaction(pubKeySender.getEncoded(), pubKeyReceiver.getEncoded(), 35, 10);
		byte[] signature = Signer.sign(trans, privKeySender);
		trans.setSignature(signature);

		// this line would fail the verification
		// trans.setSender(pubKeyReceiver.getEncoded());

		Signer.verify(trans, signature);
	}

}
