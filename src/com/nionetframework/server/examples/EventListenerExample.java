package com.nionetframework.server.examples;

import com.nionetframework.server.events.ConnectionCloseEvent;
import com.nionetframework.server.events.ServerEventDispatcher;
import com.nionetframework.server.events.ServerEventListener;
import com.nionetframework.server.events.ServerEvents;

public class EventListenerExample implements ServerEventListener {

	public static void main(String[] args) {
		EventListenerExample e = new EventListenerExample();
		ServerEventDispatcher.callEvent(new ConnectionCloseEvent(null,
				"example reason"));
	}

	public EventListenerExample() {
		ServerEventDispatcher.registerListener(this);
	}

	@ServerEvents
	public void onConnectionCloseEvent(ConnectionCloseEvent c) {
		System.out.println("The connection was closed! Reason: " + c.getInfo());
	}

}
