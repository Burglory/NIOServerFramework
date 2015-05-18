package com.nionetframework.server.api;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface NetworkThread extends Runnable {

	void terminate();

	ConcurrentLinkedQueue<PacketInbound> getInboundQueue();

	ConcurrentLinkedQueue<PacketOutbound> getOutboundQueue();

}
