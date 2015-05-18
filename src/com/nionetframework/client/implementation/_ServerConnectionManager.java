package com.nionetframework.client.implementation;

import java.nio.channels.SocketChannel;

import com.nionetframework.client.api.ServerConnectionManager;
import com.nionetframework.common.api.Connection;
import com.nionetframework.common.api.NetworkThread;
import com.nionetframework.common.implementation._ConnectionManager;
import com.nionetframework.server.api.ServerNetworkThread;
import com.nionetframework.server.implementation._Server;

public class _ServerConnectionManager extends _ConnectionManager implements ServerConnectionManager {

	private _Client client;

	public _ServerConnectionManager(_Client client) {
		super();
		this.client = client;
	}

	@Override
	public boolean disconnect(Connection c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Connection addConnection(SocketChannel s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkThread getNetworkThread() {
		// TODO Auto-generated method stub
		return client.getNetworkThread();
	}

}
