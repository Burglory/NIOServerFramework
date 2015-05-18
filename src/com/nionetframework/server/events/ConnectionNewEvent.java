package com.nionetframework.server.events;

import com.nionetframework.common.api.Connection;

public class ConnectionNewEvent extends ConnectionEvent {

	public ConnectionNewEvent(Connection c) {
		super(c);
	}

}
