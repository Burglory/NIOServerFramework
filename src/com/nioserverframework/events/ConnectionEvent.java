package com.nioserverframework.events;

import com.nioserverframework.api.Connection;

public abstract class ConnectionEvent extends ServerEvent {

	private final Connection connection;

	public ConnectionEvent(Connection c) {
		this.connection = c;
	}

	public Connection getConnection() {
		return connection;
	}
	
}
