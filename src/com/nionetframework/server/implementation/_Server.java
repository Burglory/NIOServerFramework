package com.nionetframework.server.implementation;

import com.nionetframework.common.api.ConnectionManager;
import com.nionetframework.common.api.NetworkThread;
import com.nionetframework.common.implementation._ConnectionManager;
import com.nionetframework.common.logger.Logger;
import com.nionetframework.server.api.Server;
import com.nionetframework.server.util.ServerDefaults;

public class _Server implements Server {

	private _ConnectionManager connectionmanager;
	private boolean terminate;
	private _ServerNetworkThread networkthread;

	public _Server() {
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
	public void start() {
		this.networkthread = new _ServerNetworkThread(this, ServerDefaults.PORT);
		Logger.Log("Starting Server...", Logger.MESSAGE);
		this.networkthread.run();
	}

	@Override
	public void start(String port) {
		this.networkthread = new _ServerNetworkThread(this, port);
		Logger.Log("Starting Server...", Logger.MESSAGE);
		this.networkthread.run();
	}

	@Override
	public NetworkThread getNetworkThread() {
		return this.networkthread;
	}
}
