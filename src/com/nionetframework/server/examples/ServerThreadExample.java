package com.nionetframework.server.examples;

import com.nionetframework.common.api.PacketInbound;
import com.nionetframework.server.api.ServerNetworkThread;
import com.nionetframework.server.api.Server;
import com.nionetframework.server.implementation._ServerNetworkThread;

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
			ServerNetworkThread networkthread = (ServerNetworkThread) server.getNetworkThread();
			PacketInbound p = ((_ServerNetworkThread) networkthread).getInboundQueue().poll();
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
