package com.nionetframework.common;

import java.util.Collection;

public class _PacketOutbound extends _Packet implements PacketOutbound {

	private Collection<Connection> destinations;

	public _PacketOutbound(String s, Collection<Connection> destinations) {
		super(s);
		this.destinations = destinations;
	}

	@Override
	public Collection<Connection> getDestinations() {
		return this.destinations;
	}

}
