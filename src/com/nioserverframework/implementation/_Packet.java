package com.nioserverframework.implementation;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.nioserverframework.api.Connection;
import com.nioserverframework.api.Packet;

public class _Packet implements Packet {

	private int type;
	private String data;
	public static final Charset charset = StandardCharsets.UTF_8;

	public _Packet(String s) {
		this.data = s;
	}

	@Override
	public int getType() {
		return this.type;
	}

	@Override
	public String getData() {
		return this.data;
	}
	
	@Override
	public byte[] getBytes() {
		return this.data.getBytes();
	}

}
