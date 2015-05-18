package com.nionetframework.server.examples;

import com.nionetframework.server.api.NetworkThread;
import com.nionetframework.server.api.PacketInbound;
import com.nionetframework.server.api.Server;

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
			NetworkThread networkthread = server.getNetworkThread();
			PacketInbound p = networkthread.getInboundQueue().poll();
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
