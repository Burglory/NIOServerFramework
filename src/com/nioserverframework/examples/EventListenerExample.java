package com.nioserverframework.examples;

import com.nioserverframework.events.ConnectionCloseEvent;
import com.nioserverframework.events.ServerEventDispatcher;
import com.nioserverframework.events.ServerEventListener;
import com.nioserverframework.events.ServerEvents;

public class EventListenerExample implements ServerEventListener {

	public static void main(String[] args) {
		EventListenerExample e = new EventListenerExample();
		ServerEventDispatcher.callEvent(new ConnectionCloseEvent(null, "example reason"));
	}
	
	public EventListenerExample() {
		ServerEventDispatcher.registerListener(this);
	}
	
	@ServerEvents
	public void onConnectionCloseEvent(ConnectionCloseEvent c) {
		System.out.println("The connection was closed! Reason: " + c.getInfo());
	}
	
}
