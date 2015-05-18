package com.nionetframework.server.examples;

import com.nionetframework.common.ConnectionManager;
import com.nionetframework.common.NetworkThread;
import com.nionetframework.common._ConnectionManager;
import com.nionetframework.common.logger.Logger;
import com.nionetframework.server.Server;
import com.nionetframework.server.Servers;

public class ServerExample {

	public static void main(String[] args) {
		Logger.setLogLevel(Logger.DEBUG);
		ServerExample s = new ServerExample();
	}
	private Server server;

	public ServerExample() {
		this.server = Servers.getDefaultServer();
		Thread t  = new Thread(server);
		t.start();
	}

}
