package com.nioserverframework.implementation;

import com.nioserverframework.api.Connection;
import com.nioserverframework.api.PacketInbound;

public class _PacketInbound extends _Packet implements PacketInbound {

	private Connection source;

	public _PacketInbound(String s, Connection source) {
		super(s);
		this.source = source;
	}
	
	public _PacketInbound(byte[] packet, _Connection source) {
		super(new String(packet, _Packet.charset));
		this.source = source;
	}

	@Override
	public Connection getSource() {
		return this.source;
	}

}
