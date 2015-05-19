package com.nionetframework.server;

import java.net.InetSocketAddress;

import com.nionetframework.common.ConnectionManager;
import com.nionetframework.common._ConnectionManager;
import com.nionetframework.common.logger.NetworkLogger;

class _Server extends Server {

	private _ConnectionManager connectionmanager;
	private boolean terminate;
	private _ServerNetworkThread networkthread;
	private InetSocketAddress address;
	private Thread thread;

	_Server() {
		NetworkLogger.Log("Creating Server...", NetworkLogger.MESSAGE);

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
			NetworkLogger.Log(
					"Server cannot be terminated, because it has not started!",
					NetworkLogger.WARNING);
		}
	}

	@Override
	public ServerNetworkThread getNetworkThread() {
		return this.networkthread;
	}

	@Override
	public void run() {
		this.networkthread = new _ServerNetworkThread(this, address);
		NetworkLogger.Log("Entering ServerNetworkThread...", NetworkLogger.MESSAGE);
		this.networkthread.run();
		NetworkLogger.Log("Exiting ServerNetworkThread...", NetworkLogger.MESSAGE);
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
