package com.nionetframework.server;


public class Servers {

	public static final Server getDefaultServer() {
		Server server = new _Server();
		server.setInetAddress("localhost", 8500);
		return server;
	}
	
}
