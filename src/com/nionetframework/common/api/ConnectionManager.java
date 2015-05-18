package com.nionetframework.common.api;

import java.nio.channels.SocketChannel;

public interface ConnectionManager {

	/** Disconnects the client associated with this Connection. */
	boolean disconnect(Connection c);

	/** Add a new Connection to this ConnectionManager based on a SocketChannel. */
	Connection addConnection(SocketChannel s);
	
	NetworkThread getNetworkThread();

}
