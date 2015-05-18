package com.nionetframework.server.api;

public interface Server {

	ConnectionManager getConnectionManager();

	void terminate();

	void start();

	void start(String port);

	NetworkThread getNetworkThread();

}
