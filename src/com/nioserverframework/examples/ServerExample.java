package com.nioserverframework.examples;

import com.nioserverframework.api.Server;
import com.nioserverframework.implementation._Server;
import com.nioserverframework.logger.ServerLogger;

public class ServerExample {

	public static void main(String[] args) {
		ServerLogger.setLogLevel(ServerLogger.DEBUG);
		Server s = new _Server();
		s.start();
	}

}
