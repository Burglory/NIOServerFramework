package com.nionetframework.common.api;

public interface Packet {

	int getType();

	String getData();

	byte[] getBytes();
}
