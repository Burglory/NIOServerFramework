package com.nionetframework.common;

import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.nionetframework.common.logger.NetworkLogger;

public abstract class _ConnectionManager implements ConnectionManager {

	private Set<Connection> connections;

	public _ConnectionManager() {
		NetworkLogger.Log("Initializing ConnectionManager...", NetworkLogger.MESSAGE);

		this.connections = Collections
				.newSetFromMap(new ConcurrentHashMap<Connection, Boolean>());
		NetworkLogger.Log("ConnectionManager succesfully initialized.", NetworkLogger.MESSAGE);
	}

	@Override
	public abstract boolean disconnect(Connection c);

	@Override
	public abstract Connection addConnection(SocketChannel s);

	@Override
	public abstract NetworkThread getNetworkThread();

	public Set<Connection> _getConnections() {
		return this.connections;
	}

}
