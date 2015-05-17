package com.burglory.generic.multiplayer.server.util.encryption;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	public byte[] iv;
	public byte[] ciphertext;

	public static SecretKey generateRandomKey() {
		SecureRandom sr = new SecureRandom();
		byte[] salt = new byte[8];
		byte[] password = new byte[128];
		sr.nextBytes(salt);
		sr.nextBytes(password);

		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(new String(password).toCharArray(),
					salt, 65536, 128);
			// 128 instead of 256 because of Legal limits US.
			SecretKey tmp;
			tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
			return secret;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SecretKey generateKey(byte[] key) {
		return new SecretKeySpec(key, "AES");
	}

	public byte[] encrypt(SecretKey key, byte[] text) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			AlgorithmParameters params = cipher.getParameters();
			iv = params.getParameterSpec(IvParameterSpec.class).getIV();
			ciphertext = cipher.doFinal(text);
			return ciphertext;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public byte[] decrypt(byte[] ciphertext, Key key, byte[] iv) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
			byte[] plain = cipher.doFinal(ciphertext);
			return plain;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
