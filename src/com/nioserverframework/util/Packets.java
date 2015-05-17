package com.nioserverframework.util;

import java.util.Collection;

import com.nioserverframework.api.Connection;
import com.nioserverframework.api.Packet;
import com.nioserverframework.api.PacketInbound;
import com.nioserverframework.api.PacketOutbound;
import com.nioserverframework.implementation._Packet;
import com.nioserverframework.implementation._PacketInbound;
import com.nioserverframework.implementation._PacketOutbound;

public class Packets {

	public static final Packet generatePacket(String e) {
		return new _Packet(e);
	}
	
	public static final PacketInbound generateInboundPacket(String e, Connection c) {
		return new _PacketInbound(e, c);
	}
	
	public static final PacketOutbound generateOutboundPacket(String e, Collection<Connection> c) {
		return new _PacketOutbound(e, c);
	}
	
}
