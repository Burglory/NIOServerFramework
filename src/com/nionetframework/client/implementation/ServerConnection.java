package com.nionetframework.client.implementation;

import java.nio.channels.SocketChannel;
import java.util.Queue;

import com.nionetframework.common.api.Packet;
import com.nionetframework.common.implementation._Connection;
import com.nionetframework.common.implementation._ConnectionManager;

public class ServerConnection extends _Connection {

	public ServerConnection(_ConnectionManager m, SocketChannel s) {
		super(m, s);
	}
	
	public Queue<Packet> getQueue() {
		return this.queue;
	}

}
