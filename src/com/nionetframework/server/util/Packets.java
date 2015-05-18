package com.nionetframework.server.util;

import java.util.Collection;

import com.nionetframework.server.api.Connection;
import com.nionetframework.server.api.Packet;
import com.nionetframework.server.api.PacketInbound;
import com.nionetframework.server.api.PacketOutbound;
import com.nionetframework.server.implementation._Packet;
import com.nionetframework.server.implementation._PacketInbound;
import com.nionetframework.server.implementation._PacketOutbound;

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
