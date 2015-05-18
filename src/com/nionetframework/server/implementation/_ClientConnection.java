package com.nionetframework.server.implementation;

import java.nio.channels.SocketChannel;

import com.nionetframework.common.api.ConnectionManager;
import com.nionetframework.common.implementation._Connection;
import com.nionetframework.server.api.ClientConnection;
import com.nionetframework.server.api.Server;

public class _ClientConnection extends _Connection implements ClientConnection {

	private Server server;
	
	public _ClientConnection(_ClientConnectionManager m, SocketChannel socket) {
		super(m, socket);
		this.server = m.getServer();
	}

}
