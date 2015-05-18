package com.nionetframework.common;

import java.util.Collection;

import com.nionetframework.common.api.Connection;
import com.nionetframework.common.api.Packet;
import com.nionetframework.common.api.PacketInbound;
import com.nionetframework.common.api.PacketOutbound;
import com.nionetframework.common.implementation._Packet;
import com.nionetframework.common.implementation._PacketInbound;
import com.nionetframework.common.implementation._PacketOutbound;

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
