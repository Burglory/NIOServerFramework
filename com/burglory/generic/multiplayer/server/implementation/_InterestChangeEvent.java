package com.burglory.generic.multiplayer.server.implementation;

import java.nio.channels.SocketChannel;

import com.burglory.generic.multiplayer.server.api.InterestChangeEvent;

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
