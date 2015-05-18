package com.nionetframework.common.api;

import com.nionetframework.common.implementation._Connection;

public interface NetworkThread extends Runnable {

	void offer(PacketOutbound p);

	PacketInbound poll();
	
}
