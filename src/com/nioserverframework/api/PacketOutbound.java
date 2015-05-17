package com.nioserverframework.api;

import java.util.Collection;

public interface PacketOutbound extends Packet {

	Collection<Connection> getDestinations();
	
}
