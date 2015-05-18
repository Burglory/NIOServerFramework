package com.nionetframework.common.implementation;

import java.nio.channels.SocketChannel;

import com.nionetframework.common.api.InterestChangeEvent;

public class _InterestChangeEvent implements InterestChangeEvent {

	private final int interests;
	private final SocketChannel socket;

	public _InterestChangeEvent(SocketChannel socket, int newinterests) {
		this.socket = socket;
		this.interests = newinterests;
	}

	public int getInterests() {
		return interests;
	}

	public SocketChannel getSocket() {
		return socket;
	}

}
