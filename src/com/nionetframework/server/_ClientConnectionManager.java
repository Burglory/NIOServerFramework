package com.nionetframework.server;

import java.nio.channels.SocketChannel;

import com.nionetframework.common.Connection;
import com.nionetframework.common.NetworkThread;
import com.nionetframework.common._Connection;
import com.nionetframework.common._ConnectionManager;
import com.nionetframework.common.logger.NetworkLogger;
import com.nionetframework.server.events.ConnectionCloseEvent;
import com.nionetframework.server.events.ConnectionNewEvent;
import com.nionetframework.server.events.ServerEventDispatcher;

class _ClientConnectionManager extends _ConnectionManager implements
		ClientConnectionManager {

	private Server server;

	_ClientConnectionManager(Server server) {
		super();
		this.server = server;
	}

	@Override
	public Server getServer() {
		return this.server;
	}

	@Override
	public NetworkThread getNetworkThread() {
		return server.getNetworkThread();
	}

	@Override
	public boolean disconnect(Connection c) {
		((_Connection) c)._terminateSocketChannel();
		this._getConnections().remove(((_Connection) c));
		NetworkLogger.Log("(ConnectionManager): Connection from: " + c.getAddress()
				+ " has been removed.", NetworkLogger.DEBUG);
		ServerEventDispatcher.callEvent(new ConnectionCloseEvent(c, "closed"));
		return false;
	}

	@Override
	public Connection addConnection(SocketChannel s) {
		_Connection c = new _ClientConnection(this, s);
		this._getConnections().add(c);
		NetworkLogger.Log("(ConnectionManager): Connection from: " + c.getAddress()
				+ " has been added.", NetworkLogger.DEBUG);
		ServerEventDispatcher.callEvent(new ConnectionNewEvent(c));
		return c;
	}

}
