package com.nioserverframework.api;

public interface Server {

	ConnectionManager getConnectionManager();

	boolean terminate();

	boolean start();

	boolean start(String port);

	NetworkThread getNetworkThread();

}
