package com.nionetframework.server.events;

public class QueueSizeHighOutboundEvent extends QueueSizeEvent {

	public QueueSizeHighOutboundEvent(int currentsize, int maxsize) {
		super(currentsize, maxsize);
	}

}
