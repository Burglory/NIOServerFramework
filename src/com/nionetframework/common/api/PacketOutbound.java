package com.nionetframework.common.api;

import java.util.Collection;

public interface PacketOutbound extends Packet {

	Collection<Connection> getDestinations();

}
