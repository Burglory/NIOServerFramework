package com.nionetframework.server.examples;

import com.nionetframework.server.api.Server;
import com.nionetframework.server.implementation._Server;
import com.nionetframework.server.logger.ServerLogger;

public class ServerExample {

	public static void main(String[] args) {
		ServerLogger.setLogLevel(ServerLogger.DEBUG);
		Server s = new _Server();
		s.start();
	}

}
