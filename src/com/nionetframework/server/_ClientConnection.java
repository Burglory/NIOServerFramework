package com.nionetframework.server;

import java.nio.channels.SocketChannel;

import com.nionetframework.common._Connection;

class _ClientConnection extends _Connection implements ClientConnection {

	private Server server;

	_ClientConnection(_ClientConnectionManager m, SocketChannel socket) {
		super(m, socket);
		this.server = m.getServer();
	}

}
