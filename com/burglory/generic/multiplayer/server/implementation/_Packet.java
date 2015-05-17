package com.burglory.generic.multiplayer.server.implementation;

import com.burglory.generic.multiplayer.server.api.Connection;
import com.burglory.generic.multiplayer.server.api.Packet;

public class _Packet implements Packet {

	private int type;
	private String data;
	private _Connection source;

	public _Packet(_Connection source, int type, String data) {
		this.source = source;
		this.type = type;
		this.data = data;
	}

	@Override
	public int getType() {
		return this.type;
	}

	@Override
	public String getData() {
		return this.data;
	}

	@Override
	public Connection getSource() {
		return this.source;
	}

}
