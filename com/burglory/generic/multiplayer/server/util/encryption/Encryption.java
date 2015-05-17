package com.burglory.generic.multiplayer.server.util.encryption;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import mexica.core.utilities.DataString;

import com.burglory.generic.multiplayer.server.util.data.DataBuilder;

public class Encryption {

	private PrivateKey privatekey;
	private PublicKey publickey;

	public static void main(String[] args) throws UnsupportedEncodingException {
		Encryption encryption = new Encryption();
		String username = "test";
		String password = "AMNDK@OI@;.sv[e";
		String hashedpassword = new String(SHA256.hash(password));
		String hashedpassword_base64 = new String(Base64.encode(hashedpassword
				.getBytes()));
		System.out.println("Testing: username: " + username
				+ " hashedpassword: " + hashedpassword);

		String wrapped = encryption.generatePacketData(username,
				hashedpassword_base64.getBytes());
		String unwrapped = encryption.unwrapEncryptedPacketData(wrapped);

		System.out.println(wrapped);
		System.out.println(unwrapped);

		System.out.println("Result: username: "
				+ new DataString(unwrapped).get("u") + "\n password: "
				+ Base64.decodeString(new DataString(unwrapped).get("p")));
	}

	public void generateKeys() {
		KeyPair RSA_Keypair = RSA.generateAndReturnKeyPair();
		this.privatekey = RSA_Keypair.getPrivate();
		this.publickey = RSA_Keypair.getPublic();
	}

	public String generatePacketData(String username, byte[] passwordhash_base64) {
		System.out.println("Wrapping...");

		byte[] unencrypted_data = new DataBuilder().add("u").wrap(username)
				.add("p").wrap(new String(passwordhash_base64)).getString()
				.getBytes();

		SecretKey aeskey = AES.generateRandomKey();
		System.out.println("SecretKey: "
				+ new String(Base64.encode(aeskey.getEncoded())));
		byte[] unencrypted_aeskey = aeskey.getEncoded();
		System.out.println("unencrypted_aeskey: "
				+ new String(Base64.encode(unencrypted_aeskey)));
		AES aesutil = new AES();
		byte[] encrypted_data = aesutil.encrypt(aeskey, unencrypted_data);
		System.out.println("encrypted_data: "
				+ new String(Base64.encode(encrypted_data)));
		byte[] iv = aesutil.iv;
		System.out.println("iv: " + new String(Base64.encode(iv)));

		this.generateKeys();
		byte[] encrypted_aeskey = RSA.encrypt(unencrypted_aeskey, publickey);
		System.out.println("encrypted_aeskey: "
				+ new String(Base64.encode(encrypted_aeskey)));

		String encrypted_data_base64 = new String(Base64.encode(encrypted_data));
		String iv_base64 = new String(Base64.encode(iv));
		String encrypted_aeskey_base64 = new String(
				Base64.encode(encrypted_aeskey));

		return new DataBuilder().add("data").wrap(encrypted_data_base64)
				.add("iv").wrap(iv_base64).add("key")
				.wrap(encrypted_aeskey_base64).getString();
	}

	public String unwrapEncryptedPacketData(String packet) {
		System.out.println("Unwrapping...");
		DataString pack = new DataString(packet);
		String encrypted_data_base64 = pack.get("data");
		String iv_base64 = pack.get("iv");
		String encrypted_aeskey_base64 = pack.get("key");

		byte[] encrypted_data = Base64.decode(encrypted_data_base64);
		System.out.println("encrypted_data: "
				+ new String(Base64.encode(encrypted_data)));
		byte[] iv = Base64.decode(iv_base64);
		System.out.println("iv: " + new String(Base64.encode(iv)));
		byte[] encrypted_aeskey = Base64.decode(encrypted_aeskey_base64);
		System.out.println("encrypted_aeskey: "
				+ new String(Base64.encode(encrypted_aeskey)));

		byte[] unencrypted_aeskey = RSA.decrypt(encrypted_aeskey, privatekey);
		System.out.println("unencrypted_aeskey: "
				+ new String(Base64.encode(unencrypted_aeskey)));
		SecretKey aeskey = AES.generateKey(unencrypted_aeskey);
		System.out.println("SecretKey: "
				+ new String(Base64.encode(aeskey.getEncoded())));
		byte[] unencrypted_data = new AES().decrypt(encrypted_data, aeskey, iv);

		return new DataBuilder().add("data").wrap(new String(unencrypted_data))
				.getString();
	}

}
