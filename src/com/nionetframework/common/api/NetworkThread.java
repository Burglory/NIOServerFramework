package com.nionetframework.common.api;


public interface NetworkThread extends Runnable {

	void offer(PacketOutbound p);

	PacketInbound poll();
	
}
