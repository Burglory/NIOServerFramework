package com.nionetframework.server;

import com.nionetframework.common.ConnectionManager;

public abstract class Server implements Runnable {

	public abstract ConnectionManager getConnectionManager();

	public abstract void terminate();

	public abstract ServerNetworkThread getNetworkThread();

	public abstract void setInetAddress(String hostname, int port);

	public abstract void start();

	public abstract Thread getThread();

	public static final Server getDefaultServer() {
		Server server = new _Server();
		server.setInetAddress("localhost", 8500);
		return server;
	}

}
