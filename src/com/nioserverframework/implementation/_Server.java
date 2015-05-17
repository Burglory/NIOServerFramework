package com.nioserverframework.implementation;

import com.nioserverframework.api.ConnectionManager;
import com.nioserverframework.api.NetworkThread;
import com.nioserverframework.api.Server;
import com.nioserverframework.logger.ServerLogger;
import com.nioserverframework.util.ServerDefaults;

public class _Server implements Server {

	private ConnectionManager connectionmanager;
	private boolean terminate;
	private _NetworkThread networkthread;

	public _Server() {
		ServerLogger.Log("Creating Server...", ServerLogger.MESSAGE);
		
		this.connectionmanager = new _ConnectionManager(this);
		this.networkthread = new _NetworkThread(this, ServerDefaults.PORT);
		this.terminate = false;

	}

	@Override
	public ConnectionManager getConnectionManager() {
		return this.connectionmanager;
	}

	@Override
	public boolean terminate() {
		this.terminate = true;
		this.networkthread.terminate();
		return true;
	}

	@Override
	public boolean start() {
		ServerLogger.Log("Starting Server...", ServerLogger.MESSAGE);
		this.networkthread.run();
		return true;
	}

	@Override
	public boolean start(String port) {
		this.networkthread = new _NetworkThread(this, port);
		return true;
	}

	@Override
	public NetworkThread getNetworkThread() {
		return this.networkthread;
	}
}
