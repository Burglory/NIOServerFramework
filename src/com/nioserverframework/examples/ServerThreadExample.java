package com.nioserverframework.examples;

import com.nioserverframework.api.NetworkThread;
import com.nioserverframework.api.PacketInbound;
import com.nioserverframework.api.Server;

/**Example Class that processes incoming Packets. */
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
