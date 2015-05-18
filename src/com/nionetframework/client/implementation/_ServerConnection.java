package com.nionetframework.client.implementation;

import java.nio.channels.SocketChannel;

import com.nionetframework.client.api.ServerConnection;
import com.nionetframework.common.api.ConnectionManager;
import com.nionetframework.common.implementation._Connection;
import com.nionetframework.common.implementation._ConnectionManager;

public class _ServerConnection extends _Connection implements ServerConnection {

	public _ServerConnection(_ConnectionManager m, SocketChannel s) {
		super(m, s);
	}

}
