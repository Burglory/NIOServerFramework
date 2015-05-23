package com.nionetframework.common;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Packet {

	private byte[] data;
	public static final Charset charset = StandardCharsets.UTF_8;

	public Packet(String s) {
		this.data = s.getBytes(charset);
	}
	
	public Packet(byte[] data) {
		this.data = data;
	}

	public String getData() {
		return new String(this.data, charset);
	}

	public byte[] getBytes() {
		return this.data;
	}

}
