package com.nionetframework.server.examples;

import com.nionetframework.common.logger.Logger;
import com.nionetframework.server.api.Server;
import com.nionetframework.server.implementation._Server;

public class ServerExample {

	public static void main(String[] args) {
		Logger.setLogLevel(Logger.DEBUG);
		Server s = new _Server();
		s.start();
	}

}
