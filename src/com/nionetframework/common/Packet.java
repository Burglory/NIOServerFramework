package com.nionetframework.common;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Packet {

	private String data;
	public static final Charset charset = StandardCharsets.UTF_8;

	public Packet(String s) {
		this.data = s;
	}

	public String getData() {
		return this.data;
	}

	public byte[] getBytes() {
		return this.data.getBytes();
	}

}
