package com.nioserverframework.api;

public interface Connection {

	/**
	 * Sends a {@link Packet} through this {@link Connection}.
	 * 
	 * @param p
	 *            the {@link} Packet to be send.
	 */
	boolean queue(Packet p);

	/** Returns the {@link ConnectionManager} that manages this Connection. */
	ConnectionManager getConnectionManager();

	String getAddress();

}
