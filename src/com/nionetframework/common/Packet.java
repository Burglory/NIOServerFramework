package com.nionetframework.common;

public interface Packet {

	int getType();

	String getData();

	byte[] getBytes();
}
