package com.nionetframework.server.api;

public interface Packet {

	int getType();

	String getData();

	byte[] getBytes();
}
