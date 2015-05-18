package com.nionetframework.client.implementation;

import java.net.InetSocketAddress;

import com.nionetframework.common.api.ConnectionManager;
import com.nionetframework.common.api.NetworkThread;
import com.nionetframework.common.implementation._ConnectionManager;
import com.nionetframework.common.logger.Logger;
import com.nionetframework.server.util.ServerDefaults;

public class Client {

	private _ConnectionManager connectionmanager;
	private boolean terminate;
	private ClientNetworkThread networkthread;
	
	public static final Client getDefaultClient() {
		return new Client();
	}

	Client() {
		Logger.Log("Creating Client...", Logger.MESSAGE);

		this.connectionmanager = new ServerConnectionManager(this);

		this.terminate = false;

	}

	public ConnectionManager getConnectionManager() {
		return this.connectionmanager;
	}
	
	public void terminate() {
		this.terminate = true;
		if (this.networkthread != null) {
			this.networkthread.terminate();
		} else {
			Logger.Log(
					"Client cannot be terminated, because it has not started!",
					Logger.WARNING);
		}
	}
//
//	public void start() {
//		this.networkthread = new ClientNetworkThread(this, new InetSocketAddress("localhost", Integer.parseInt(ServerDefaults.PORT)));
//		Logger.Log("Starting Client...", Logger.MESSAGE);
//		this.networkthread.run();
//	}

	public NetworkThread getNetworkThread() {
		return this.networkthread;
	}

	public void start(String ip, String port) {
		this.networkthread = new ClientNetworkThread(this, new InetSocketAddress(ip, Integer.parseInt(port)));
		
		Logger.Log("Starting Client...", Logger.MESSAGE);
		Thread  t= new Thread(this.networkthread);
		t.start();
//		Logger.Log("Stopping Client...", Logger.MESSAGE);
	}

}
