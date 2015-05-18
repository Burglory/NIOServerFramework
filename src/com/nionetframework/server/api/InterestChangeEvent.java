package com.nionetframework.server.api;

import java.nio.channels.SocketChannel;

public interface InterestChangeEvent {

	public int getInterests();

	public SocketChannel getSocket();

}
