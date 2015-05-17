package com.burglory.generic.multiplayer.server.implementation;

import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.burglory.generic.multiplayer.server.api.Connection;
import com.burglory.generic.multiplayer.server.api.ConnectionManager;
import com.burglory.generic.multiplayer.server.api.Server;
import com.burglory.generic.multiplayer.server.api.User;

public class _ConnectionManager implements ConnectionManager {

	private _Server _server;
	private Set<Connection> connections;

	public _ConnectionManager(_Server _server) {
		this._server = _server;
		this.connections = Collections
				.newSetFromMap(new ConcurrentHashMap<Connection, Boolean>());
	}

	@Override
	public Server getServer() {
		return _server;
	}

	@Override
	public boolean disconnect(User u) {
		((_Connection) getConnection(u)).stop();
		this.connections.remove(((_Connection) getConnection(u)));
		return false;
	}

	@Override
	public boolean disconnect(Connection c) {
		((_Connection) c).stop();
		this.connections.remove(((_Connection) c));
		return false;
	}

	@Override
	public User getUser(Connection c) {
		return ((_Connection) c).getUser();
	}

	@Override
	public Connection getConnection(User u) {
		for (Connection c : this.connections) {
			if (((_Connection) c).getUser().equals(u))
				return c;
		}
		return null;
	}

	@Override
	public Connection getConnection(String username) {
		for (Connection c : this.connections) {
			if (((_Connection) c).getUser().getUsername().equals(username))
				return c;
		}
		return null;
	}

	@Override
	public Connection addConnection(SocketChannel s) {
		_Connection c = new _Connection(this, s);
		this.connections.add(c);
		return c;
	}

}
