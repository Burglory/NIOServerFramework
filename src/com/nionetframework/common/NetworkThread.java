package com.nionetframework.common;


public interface NetworkThread extends Runnable {

	void offer(PacketOutbound p);

	PacketInbound poll();
	
}
