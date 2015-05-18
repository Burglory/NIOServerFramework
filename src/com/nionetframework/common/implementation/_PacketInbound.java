package com.nionetframework.common.implementation;

import com.nionetframework.common.api.Connection;
import com.nionetframework.common.api.PacketInbound;

public class _PacketInbound extends _Packet implements PacketInbound {

	private Connection source;

	public _PacketInbound(String s, Connection source) {
		super(s);
		this.source = source;
	}

	public _PacketInbound(byte[] packet, Connection source) {
		super(new String(packet, _Packet.charset));
		this.source = source;
	}

	@Override
	public Connection getSource() {
		return this.source;
	}

}
