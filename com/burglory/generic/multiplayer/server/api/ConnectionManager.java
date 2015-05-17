package com.burglory.generic.multiplayer.server.api;

import java.nio.channels.SocketChannel;

public interface ConnectionManager {

	Server getServer();

	/** Convenience method. Uses {@link #disconnect(Connection c)} internally. */
	boolean disconnect(User u);

	/** Disconnects the client associated with this Connection. */
	boolean disconnect(Connection c);

	/** Returns the {@link User} associated with the Connection. */
	User getUser(Connection c);

	/** Returns the Connection associated with the User. */
	Connection getConnection(User u);

	/**
	 * Convenience method. Uses {@link #getConnection(User)} internally. Returns
	 * the Connection associated with the username.
	 */
	Connection getConnection(String username);

	/** Add a new Connection to this ConnectionManager based on a SocketChannel. */
	Connection addConnection(SocketChannel s);

}
