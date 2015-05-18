package com.nionetframework.server.events;

import com.nionetframework.server.api.Connection;

public class ConnectionNewEvent extends ConnectionEvent {

	public ConnectionNewEvent(Connection c) {
		super(c);
	}

}
