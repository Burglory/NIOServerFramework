package com.nionetframework.client;

import java.nio.channels.SocketChannel;

import com.nionetframework.common.Connection;
import com.nionetframework.common.NetworkThread;
import com.nionetframework.common._Connection;
import com.nionetframework.common._ConnectionManager;

class _ServerConnectionManager extends _ConnectionManager implements ServerConnectionManager {

	private _Client _Client;

	_ServerConnectionManager(_Client _Client) {
		super();
		this._Client = _Client;
	}
	
	@Override
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
		_ServerConnection c = new _ServerConnection(this, s);
		this._getConnections().add(c);
		return c;
	}

	@Override
	public NetworkThread getNetworkThread() {
		// TODO Auto-generated method stub
		return _Client.getNetworkThread();
	}

}
