package com.nioserverframework.implementation;

import java.util.Collection;

import com.nioserverframework.api.Connection;
import com.nioserverframework.api.PacketOutbound;

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
