package com.nionetframework.client;

import java.net.InetSocketAddress;

import com.nionetframework.common.ConnectionManager;

public abstract class Client implements Runnable {

	/**
	 * Returns an instance of a Client with the InetSocketAddress:
	 * localhost:8500.
	 */
	public static final _Client getDefaultClient() {
		return new _Client();
	}

	/**
	 * Returns the instance of ConnectionManager that is used by the
	 * NetworkThread.
	 */
	public abstract ConnectionManager getConnectionManager();

	/** Terminates the NetworkThread and the Client. */
	public abstract void terminate();

	/** Returns the ClientNetworkThread. */
	public abstract ClientNetworkThread getNetworkThread();

	/** Sets the InetAddress of the Server this Client needs to connect to. */
	public abstract void setInetAddress(InetSocketAddress address);

	/** Stars the Client in a new Thread. */
	public abstract void start();

	/** Returns the Thread of the Client. */
	public abstract Thread getThread();

}
