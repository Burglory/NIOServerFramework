package com.nioserverframework.api;

public interface PacketInbound extends Packet {

	Connection getSource();
	
}
