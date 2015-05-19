package com.nionetframework.server.examples;

import com.nionetframework.common.PacketInbound;
import com.nionetframework.server.Server;
import com.nionetframework.server.ServerNetworkThread;

/** Example Class that processes incoming Packets. */
public abstract class ServerThreadExample implements Runnable {

	private Server server;
	private boolean terminate;

	public ServerThreadExample(Server s) {
		this.server = s;
		this.terminate = false;
	}

	@Override
	public void run() {
		while (!terminate) {
			ServerNetworkThread networkthread = (ServerNetworkThread) server
					.getNetworkThread();
			PacketInbound p = networkthread.poll();
			if (p != null) {
				/** Process Packet */
			}
		}
	}

	public void terminate() {
		this.terminate = true;
	}

	public Server getServer() {
		return this.server;
	}

}
