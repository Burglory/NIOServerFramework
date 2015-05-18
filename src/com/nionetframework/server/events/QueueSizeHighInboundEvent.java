package com.nionetframework.server.events;

public class QueueSizeHighInboundEvent extends QueueSizeEvent {

	public QueueSizeHighInboundEvent(int currentsize, int maxsize) {
		super(currentsize, maxsize);
	}

}
