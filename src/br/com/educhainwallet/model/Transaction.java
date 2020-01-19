package br.com.educhainwallet.model;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.educhainwallet.setup.PropertiesManager;

public class Transaction implements Comparable<Transaction>, Serializable {

	private static final long serialVersionUID = -8270876610064570814L;
	private static KeyFactory keyFactory;

	static{
		try {
			keyFactory = KeyFactory.getInstance(PropertiesManager.getInstance().getKeyGenAlgorithm());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	private byte[] sender, receiver, signature;
	private double amount, fee;

	private String uniqueID;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date creationTime;

	public Transaction(byte[] sender, byte[] receiver, double amount, double fee) {
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
		this.fee = fee;

		this.creationTime = new Date(System.currentTimeMillis());
		this.uniqueID = UUID.randomUUID().toString();
	}

	public Transaction(byte[] sender, byte[] receiver, double amount, double fee, Date creationTime,
			String uniqueID) {
		this(sender, receiver, amount, fee);
		this.creationTime = creationTime;
		this.uniqueID = uniqueID;
	}

	@Override
	public String toString() {
		if (creationTime != null)
			return "UniqueId: " + uniqueID + "; Sender: " + sender.hashCode() + "; Receiver: " + receiver.hashCode()
					+ "; Amount: " + amount + "; " + "Fee: " + fee + "; Creation time: "
					+ formatter.format(creationTime) + ";";
		else
			return "UniqueId: " + uniqueID + "; Sender: " + sender.hashCode() + "; Receiver: " + receiver.hashCode() + "; Amount: " + amount
					+ "; " + "Fee: " + fee + ";";
	}

	public byte[] getSender() {
		return sender;
	}

	public void setSender(byte[] sender) {
		this.sender = sender;
	}

	public byte[] getReceiver() {
		return receiver;
	}

	public void setReceiver(byte[] receiver) {
		this.receiver = receiver;
	}
	
	public byte[] getSignature() {
		return signature;
	}
	
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	
	public PublicKey getPubKey(byte[] key) {
		try {
	        return keyFactory.generatePublic(new X509EncodedKeySpec(key));
	    } catch (InvalidKeySpecException e) {
	        throw new IllegalArgumentException(e);
	    }
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	@Override
	public int hashCode() {
		return uniqueID.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (Double.doubleToLongBits(fee) != Double.doubleToLongBits(other.fee))
			return false;
		if (receiver == null) {
			if (other.receiver != null)
				return false;
		} else if (!receiver.equals(other.receiver))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		if (signature == null) {
			if (other.signature != null)
				return false;
		} else if (!signature.equals(other.signature))
			return false;
		if (uniqueID == null) {
			if (other.uniqueID != null)
				return false;
		} else if (!uniqueID.equals(other.uniqueID))
			return false;
		return true;
	}

	@Override
	public int compareTo(Transaction t) {
		if (this.fee < t.getFee())
			return 1;
		else if (this.fee > t.getFee())
			return -1;
		else {
			// transactions are only equal if they have the same uniqueID
			if (uniqueID.equals(t.getUniqueID()))
				return 0;
			else
				return 1;
		}
	}

}
