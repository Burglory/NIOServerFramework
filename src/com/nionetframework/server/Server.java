package com.nionetframework.server;

import com.nionetframework.common.ConnectionManager;
import com.nionetframework.common.NetworkThread;

public interface Server extends Runnable {

	ConnectionManager getConnectionManager();

	void terminate();

	NetworkThread getNetworkThread();

	void setInetAddress(String hostname, int port);
	
}
