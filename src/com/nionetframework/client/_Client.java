package com.nionetframework.client;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.nionetframework.common.ConnectionManager;
import com.nionetframework.common.NetworkThread;
import com.nionetframework.common._ConnectionManager;
import com.nionetframework.common.logger.Logger;

class _Client extends Client {

	private _ConnectionManager connectionmanager;
	private boolean terminate;
	private _ClientNetworkThread networkthread;
	private InetSocketAddress address;

	_Client() {
		Logger.Log("Creating _Client...", Logger.MESSAGE);

		this.connectionmanager = new _ServerConnectionManager(this);

		this.terminate = false;

	}

	@Override
	public ConnectionManager getConnectionManager() {
		return this.connectionmanager;
	}
	
	public void terminate() {
		this.terminate = true;
		if (this.networkthread != null) {
			this.networkthread.terminate();
		} else {
			Logger.Log(
					"_Client cannot be terminated, because it has not started!",
					Logger.WARNING);
		}
	}

	@Override
	public NetworkThread getNetworkThread() {
		return this.networkthread;
	}

	public void start(String ip, String port) {

		Thread  t= new Thread();
		t.start();
//		Logger.Log("Stopping _Client...", Logger.MESSAGE);
	}

	@Override
	public void run() {
		this.networkthread = new _ClientNetworkThread(this, address);
		Logger.Log("Entering NetworkThread...", Logger.MESSAGE);
		this.networkthread.run();
		Logger.Log("Exiting NetworkThread...", Logger.MESSAGE);
	}

	@Override
	public void setInetAddress(InetSocketAddress address) {
		this.address = address;
	}

}
