package com.nionetframework.client;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.nionetframework.common.ConnectionManager;
import com.nionetframework.common.NetworkThread;

public abstract class Client implements Runnable {

	public static final _Client getDefaultClient() {
		return new _Client();
	}
	
	public abstract ConnectionManager getConnectionManager();
	
	public abstract void terminate();
	
	public abstract NetworkThread getNetworkThread();
	
	public abstract void setInetAddress(InetSocketAddress address);
	
	
}
