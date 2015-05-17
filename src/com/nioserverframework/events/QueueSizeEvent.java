package com.nioserverframework.events;

public class QueueSizeEvent extends ServerEvent {

	private final int currentsize;
	private final int maxsize;

	public QueueSizeEvent(int currentsize, int maxsize) {
		super();
		this.currentsize = currentsize;
		this.maxsize = maxsize;
	}

	public int getCurrentsize() {
		return currentsize;
	}

	public int getMaxsize() {
		return maxsize;
	}
	
}
