package com.nionetframework.server.util.encryption;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

	private MessageDigest messagedigest;

	// TODO: Prevent a MessageDigest instance from being called everytime Sha256
	// is called, so:

	public SHA256() {
		try {
			this.messagedigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public byte[] hash256(String data) {
		return messagedigest.digest(data.getBytes(StandardCharsets.UTF_8));
	}

	public static byte[] hash(String data) {
		MessageDigest digest;
		byte[] passwordhash = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			passwordhash = digest.digest(data.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return passwordhash;
	}

}
