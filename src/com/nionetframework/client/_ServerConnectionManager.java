package com.nionetframework.client;

import java.nio.channels.SocketChannel;

import com.nionetframework.common.Connection;
import com.nionetframework.common.NetworkThread;
import com.nionetframework.common._Connection;
import com.nionetframework.common._ConnectionManager;
import com.nionetframework.common.logger.NetworkLogger;

class _ServerConnectionManager extends _ConnectionManager implements
		ServerConnectionManager {

	private _Client client;

	_ServerConnectionManager(_Client _Client) {
		super();
		this.client = _Client;
	}

	@Override
	public Connection getServerConnection() {
		return this._getConnections().iterator().next();
	}

	@Override
	public boolean disconnect(Connection c) {
		((_Connection) c)._terminateSocketChannel();
		this._getConnections().remove(c);
		NetworkLogger.Log("(ConnectionManager): Connection from: " + c.getAddress()
				+ " has been removed.", NetworkLogger.DEBUG);
		return false;
	}

	@Override
	public Connection addConnection(SocketChannel s) {
		_ServerConnection c = new _ServerConnection(this, s);
		this._getConnections().add(c);
		NetworkLogger.Log("(ConnectionManager): Connection from: " + c.getAddress()
				+ " has been added.", NetworkLogger.DEBUG);
		return c;
	}

	@Override
	public NetworkThread getNetworkThread() {
		return client.getNetworkThread();
	}

	@Override
	public Client getClient() {
		return this.client;
	}

}
