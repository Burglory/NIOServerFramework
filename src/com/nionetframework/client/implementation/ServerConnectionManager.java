package com.nionetframework.client.implementation;

import java.nio.channels.SocketChannel;

import com.nionetframework.common.api.Connection;
import com.nionetframework.common.api.NetworkThread;
import com.nionetframework.common.implementation._Connection;
import com.nionetframework.common.implementation._ConnectionManager;

public class ServerConnectionManager extends _ConnectionManager {

	private Client client;

	public ServerConnectionManager(Client client) {
		super();
		this.client = client;
	}
	
	public Connection getServerConnection() {
		return this._getConnections().iterator().next();
	}

	@Override
	public boolean disconnect(Connection c) {
		((_Connection) c)._terminateSocketChannel();
		this._getConnections().remove(c);
		return false;
	}

	@Override
	public Connection addConnection(SocketChannel s) {
		ServerConnection c = new ServerConnection(this, s);
		this._getConnections().add(c);
		return c;
	}

	@Override
	public NetworkThread getNetworkThread() {
		// TODO Auto-generated method stub
		return client.getNetworkThread();
	}

}
