package com.nionetframework.server.events;

import com.nionetframework.server.api.Connection;

public abstract class ConnectionEvent extends ServerEvent {

	private final Connection connection;

	public ConnectionEvent(Connection c) {
		this.connection = c;
	}

	public Connection getConnection() {
		return connection;
	}

}
