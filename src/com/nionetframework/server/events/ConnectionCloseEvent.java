package com.nionetframework.server.events;

import com.nionetframework.server.api.Connection;

public class ConnectionCloseEvent extends ConnectionEvent {

	private final String info;

	public ConnectionCloseEvent(Connection c, String info) {
		super(c);
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

}
