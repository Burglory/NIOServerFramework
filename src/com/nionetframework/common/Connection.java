package com.nionetframework.common;

public abstract interface Connection {

	/** Returns the {@link ConnectionManager} that manages this Connection. */
	ConnectionManager getConnectionManager();

	String getAddress();

}
