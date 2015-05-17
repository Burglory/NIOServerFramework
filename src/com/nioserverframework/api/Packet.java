package com.nioserverframework.api;

public interface Packet {

	int getType();

	String getData();

	byte[] getBytes();
}
