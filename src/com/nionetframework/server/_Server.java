package com.nionetframework.server;

import java.net.InetSocketAddress;

import com.nionetframework.common.ConnectionManager;
import com.nionetframework.common._ConnectionManager;
import com.nionetframework.common.logger.Logger;

class _Server extends Server {

	private _ConnectionManager connectionmanager;
	private boolean terminate;
	private _ServerNetworkThread networkthread;
	private InetSocketAddress address;
	private Thread thread;

	_Server() {
		Logger.Log("Creating Server...", Logger.MESSAGE);

		this.connectionmanager = new _ClientConnectionManager(this);

		this.terminate = false;

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
			Logger.Log(
					"Server cannot be terminated, because it has not started!",
					Logger.WARNING);
		}
	}

	@Override
	public ServerNetworkThread getNetworkThread() {
		return this.networkthread;
	}

	@Override
	public void run() {
		this.networkthread = new _ServerNetworkThread(this, address);
		Logger.Log("Entering ServerNetworkThread...", Logger.MESSAGE);
		this.networkthread.run();
		Logger.Log("Exiting ServerNetworkThread...", Logger.MESSAGE);
	}

	@Override
	public void setInetAddress(String hostname, int port) {
		this.address = new InetSocketAddress(hostname, port);
	}

	@Override
	public void start() {
		this.thread = new Thread(this);
		this.thread.start();
	}

	@Override
	public Thread getThread() {
		return this.thread;
	}
}
