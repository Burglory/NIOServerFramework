package com.nionetframework.common;

public class PacketInbound extends Packet {

	private Connection source;

	public PacketInbound(byte[] packet, Connection source) {
		super(packet);
		this.source = source;
	}

	public Connection getSource() {
		return this.source;
	}

}
