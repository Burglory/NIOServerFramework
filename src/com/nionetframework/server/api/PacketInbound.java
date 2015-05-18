package com.nionetframework.server.api;

public interface PacketInbound extends Packet {

	Connection getSource();

}
