package com.nionetframework.common;

import java.util.Collection;

public interface PacketOutbound extends Packet {

	Collection<Connection> getDestinations();

}
