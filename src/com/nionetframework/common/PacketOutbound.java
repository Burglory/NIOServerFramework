package com.nionetframework.common;

import java.util.Arrays;
import java.util.Collection;

public class PacketOutbound extends Packet {

	private Collection<Connection> destinations;

	public PacketOutbound(byte[] data, Collection<Connection> destinations) {
		super(data);
		this.destinations = destinations;
	}

	public PacketOutbound(byte[] data, Connection destination) {
		super(data);
		this.destinations = Arrays.asList(destination);
	}
	
	public Collection<Connection> getDestinations() {
		return this.destinations;
	}

}
