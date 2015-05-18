package com.nionetframework.client.api;

import com.nionetframework.common.api.NetworkThread;

public interface Client {

	void start();
	
	void start(String ip, String port);
	
	void terminate();
	
	NetworkThread getNetworkThread();
	
}
