package com.nioserverframework.events;

import com.nioserverframework.api.Connection;

public class ConnectionNewEvent extends ConnectionEvent {

	public ConnectionNewEvent(Connection c) {
		super(c);
	}

}
