package com.nionetframework.common.implementation;

import java.util.Collection;

import com.nionetframework.common.api.Connection;
import com.nionetframework.common.api.PacketOutbound;

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
