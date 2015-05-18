package com.nionetframework.common;

import java.util.Collection;

public class Packets {

	public static final Packet generatePacket(String e) {
		return new _Packet(e);
	}

	public static final PacketInbound generateInboundPacket(String e,
			Connection c) {
		return new _PacketInbound(e, c);
	}

	public static final PacketOutbound generateOutboundPacket(String e,
			Collection<Connection> c) {
		return new _PacketOutbound(e, c);
	}

}
