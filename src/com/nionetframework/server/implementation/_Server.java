package com.nionetframework.server.implementation;

import com.nionetframework.server.api.ConnectionManager;
import com.nionetframework.server.api.NetworkThread;
import com.nionetframework.server.api.Server;
import com.nionetframework.server.logger.ServerLogger;
import com.nionetframework.server.util.ServerDefaults;

public class _Server implements Server {

	private ConnectionManager connectionmanager;
	private boolean terminate;
	private _NetworkThread networkthread;

	public _Server() {
		ServerLogger.Log("Creating Server...", ServerLogger.MESSAGE);

		this.connectionmanager = new _ConnectionManager(this);

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
			ServerLogger.Log(
					"Server cannot be terminated, because it has not started!",
					ServerLogger.WARNING);
		}
	}

	@Override
	public void start() {
		this.networkthread = new _NetworkThread(this, ServerDefaults.PORT);
		ServerLogger.Log("Starting Server...", ServerLogger.MESSAGE);
		this.networkthread.run();
	}

	@Override
	public void start(String port) {
		this.networkthread = new _NetworkThread(this, port);
		ServerLogger.Log("Starting Server...", ServerLogger.MESSAGE);
		this.networkthread.run();
	}

	@Override
	public NetworkThread getNetworkThread() {
		return this.networkthread;
	}
}
