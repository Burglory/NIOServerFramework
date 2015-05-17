package com.burglory.generic.multiplayer.server.api;

public interface Packet {

	int getType();

	String getData();

	Connection getSource();
}
