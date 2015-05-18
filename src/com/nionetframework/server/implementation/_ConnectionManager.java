package com.nionetframework.server.implementation;

import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.nionetframework.server.api.Connection;
import com.nionetframework.server.api.ConnectionManager;
import com.nionetframework.server.api.Server;
import com.nionetframework.server.logger.ServerLogger;

public class _ConnectionManager implements ConnectionManager {

	private _Server _server;
	private Set<Connection> connections;

	public _ConnectionManager(_Server _server) {
		ServerLogger.Log("Initializing ConnectionManager...",
				ServerLogger.MESSAGE);
		this._server = _server;
		this.connections = Collections
				.newSetFromMap(new ConcurrentHashMap<Connection, Boolean>());
		ServerLogger.Log("ConnectionManager succesfully initialized.",
				ServerLogger.MESSAGE);
	}

	@Override
	public Server getServer() {
		return _server;
	}

	@Override
	public boolean disconnect(Connection c) {
		((_Connection) c)._terminateSocketChannel();
		this.connections.remove(((_Connection) c));
		ServerLogger.Log(
				"(ConnectionManager): Connection from: " + c.getAddress()
						+ " has been removed.", ServerLogger.DEBUG);
		return false;
	}

	@Override
	public Connection addConnection(SocketChannel s) {
		_Connection c = new _Connection(this, s);
		this.connections.add(c);
		ServerLogger.Log(
				"(ConnectionManager): Connection from: " + c.getAddress()
						+ " has been added.", ServerLogger.DEBUG);
		return c;
	}

}
