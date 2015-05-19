package com.nionetframework.common;

public class PacketInbound extends Packet {

	private Connection source;

	public PacketInbound(String s, Connection source) {
		super(s);
		this.source = source;
	}

	public PacketInbound(byte[] packet, Connection source) {
		super(new String(packet, Packet.charset));
		this.source = source;
	}

	public Connection getSource() {
		return this.source;
	}

}
