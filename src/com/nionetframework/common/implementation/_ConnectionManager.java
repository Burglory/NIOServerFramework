package com.nionetframework.common.implementation;

import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.nionetframework.common.api.Connection;
import com.nionetframework.common.api.ConnectionManager;
import com.nionetframework.common.api.NetworkThread;
import com.nionetframework.common.logger.Logger;

public abstract class _ConnectionManager implements ConnectionManager {

	private Set<Connection> connections;

	public _ConnectionManager() {
		Logger.Log("Initializing ConnectionManager...",
				Logger.MESSAGE);

		this.connections = Collections
				.newSetFromMap(new ConcurrentHashMap<Connection, Boolean>());
		Logger.Log("ConnectionManager succesfully initialized.",
				Logger.MESSAGE);
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
