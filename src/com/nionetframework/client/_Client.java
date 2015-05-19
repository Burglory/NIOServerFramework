package com.nionetframework.client;

import java.net.InetSocketAddress;

import com.nionetframework.common.ConnectionManager;
import com.nionetframework.common._ConnectionManager;
import com.nionetframework.common.logger.NetworkLogger;

class _Client extends Client {

	private _ConnectionManager connectionmanager;
	private boolean terminate;
	private _ClientNetworkThread networkthread;
	private InetSocketAddress address;
	private Thread thread;

	_Client() {
		NetworkLogger.Log("Creating _Client...", NetworkLogger.MESSAGE);

		this.connectionmanager = new _ServerConnectionManager(this);

	}

	@Override
	public ConnectionManager getConnectionManager() {
		return this.connectionmanager;
	}

	@Override
	public void terminate() {
		this.terminate = true;
		if (this.networkthread != null) {
			this.networkthread.terminate();
		} else {
			NetworkLogger.Log(
					"_Client cannot be terminated, because it has not started!",
					NetworkLogger.WARNING);
		}
	}

	@Override
	public ClientNetworkThread getNetworkThread() {
		return this.networkthread;
	}

	@Override
	public void run() {
		if (this.terminate == true)
			return;
		this.terminate = false;
		this.networkthread = new _ClientNetworkThread(this, address);
		NetworkLogger.Log("Entering NetworkThread...", NetworkLogger.MESSAGE);
		this.networkthread.run();
		NetworkLogger.Log("Exiting NetworkThread...", NetworkLogger.MESSAGE);
	}

	@Override
	public void setInetAddress(InetSocketAddress address) {
		this.address = address;
	}

	@Override
	public void start() {
		this.thread = new Thread(this);
		thread.start();
	}

	@Override
	public Thread getThread() {
		return this.thread;
	}

}
