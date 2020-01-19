import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

import br.com.educhainwallet.KeyUtils;
import br.com.educhainwallet.Signer;
import br.com.educhainwallet.model.Transaction;
import br.com.educhainwallet.setup.PropertiesManager;
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

		Transaction trans = new Transaction(pubKeySender.getEncoded(), pubKeyReceiver.getEncoded(),
				PropertiesManager.getInstance().getTransAmount(), PropertiesManager.getInstance().getTransFee());
		
		if(signAndVerify(trans, privKeySender))
			sendTransaction(trans, privKeySender);
	}

	public static void sendTransaction(Transaction trans, PrivateKey privKeySender) {

		byte[] signature = Signer.sign(trans, privKeySender);
		trans.setSignature(signature);
		MemPoolUtils.sendTransaction(trans);

	}

	public static boolean signAndVerify(Transaction trans, PrivateKey privKeySender) {
		byte[] signature = Signer.sign(trans, privKeySender);
		trans.setSignature(signature);

		// this line would fail the verification
		// trans.setSender(pubKeyReceiver.getEncoded());

		return Signer.verify(trans, signature);
	}

}
