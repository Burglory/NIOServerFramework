package com.nionetframework.server.api;

import com.nionetframework.common.api.ConnectionManager;
import com.nionetframework.common.api.NetworkThread;

public interface Server {

	ConnectionManager getConnectionManager();

	void terminate();

	void start();

	void start(String port);

	NetworkThread getNetworkThread();

}
