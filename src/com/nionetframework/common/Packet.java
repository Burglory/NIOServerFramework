package com.nionetframework.common;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Packet {

	private byte[] data;
	
	public Packet(byte[] data) {
		this.data = data;
	}

	public byte[] getBytes() {
		return this.data;
	}

}
