package com.nionetframework.client;

import java.nio.channels.SocketChannel;
import java.util.Queue;

import com.nionetframework.common.Packet;
import com.nionetframework.common._Connection;
import com.nionetframework.common._ConnectionManager;

class _ServerConnection extends _Connection implements ServerConnection {

	_ServerConnection(_ConnectionManager m, SocketChannel s) {
		super(m, s);
	}
	
	public Queue<Packet> getQueue() {
		return this.queue;
	}

}
