package com.nionetframework.client.api;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.nionetframework.common.api.PacketInbound;
import com.nionetframework.common.api.PacketOutbound;

public interface ClientNetworkThread {

	void offer(PacketOutbound p);
	
	PacketInbound poll();
	
}
