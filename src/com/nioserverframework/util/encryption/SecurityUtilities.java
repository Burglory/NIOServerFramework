package com.nioserverframework.util.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecurityUtilities {

	private static final SecureRandom random = new SecureRandom();

	public static String generateRandomToken() {
		return new BigInteger(130, random).toString(32);
	}

	static byte[] hashDBPWandToken(String databasepasswordhash, String token)
			throws NoSuchAlgorithmException {
		MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
		byte[] databasepasswordbytes = (token + databasepasswordhash)
				.getBytes();
		byte[] passhash = sha256.digest(databasepasswordbytes);
		return passhash;
	}

	public static String toHex(byte[] bytes) {
		BigInteger bi = new BigInteger(1, bytes);
		return String.format("%0" + (bytes.length << 1) + "X", bi);
	}

}
