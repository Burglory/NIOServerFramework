package com.nionetframework.server.implementation;

import java.nio.channels.SocketChannel;

import com.nionetframework.common.api.Connection;
import com.nionetframework.common.api.NetworkThread;
import com.nionetframework.common.implementation._Connection;
import com.nionetframework.common.implementation._ConnectionManager;
import com.nionetframework.common.logger.Logger;
import com.nionetframework.server.api.ClientConnectionManager;
import com.nionetframework.server.api.ServerNetworkThread;
import com.nionetframework.server.api.Server;
import com.nionetframework.server.events.ConnectionCloseEvent;
import com.nionetframework.server.events.ConnectionNewEvent;
import com.nionetframework.server.events.ServerEventDispatcher;

public class _ClientConnectionManager extends _ConnectionManager implements ClientConnectionManager {

	private _Server _server;

	public _ClientConnectionManager(_Server _server) {
		super();
		this._server = _server;
	}
	
	@Override
	public Server getServer() {
		return this._server;
	}

	@Override
	public NetworkThread getNetworkThread() {
		return _server.getNetworkThread();
	}
	
	@Override
	public boolean disconnect(Connection c) {
		((_Connection) c)._terminateSocketChannel();
		this._getConnections().remove(((_Connection) c));
		Logger.Log(
				"(ConnectionManager): Connection from: " + c.getAddress()
						+ " has been removed.", Logger.DEBUG);
		ServerEventDispatcher.callEvent(new ConnectionCloseEvent(c, "closed"));
		return false;
	}

	@Override
	public Connection addConnection(SocketChannel s) {
		_Connection c = new _ClientConnection(this, s);
		this._getConnections().add(c);
		Logger.Log(
				"(ConnectionManager): Connection from: " + c.getAddress()
						+ " has been added.", Logger.DEBUG);
		ServerEventDispatcher.callEvent(new ConnectionNewEvent(c));
		return c;
	}

}
