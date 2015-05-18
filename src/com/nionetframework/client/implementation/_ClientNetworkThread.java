package com.nionetframework.client.implementation;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.nionetframework.client.api.ClientNetworkThread;
import com.nionetframework.common.api.PacketInbound;
import com.nionetframework.common.api.PacketOutbound;
import com.nionetframework.common.implementation._NetworkThread;

public class _ClientNetworkThread extends _NetworkThread implements ClientNetworkThread {

	private ConcurrentLinkedQueue<PacketInbound> inboundqueue;
	private ConcurrentLinkedQueue<PacketOutbound> outboundqueue;

	public _ClientNetworkThread() {
		this.inboundqueue = new ConcurrentLinkedQueue<PacketInbound>();
		this.outboundqueue = new ConcurrentLinkedQueue<PacketOutbound>();
	}
	
	public ConcurrentLinkedQueue<PacketInbound> getInboundQueue() {
		return this.inboundqueue;
	}
	
	public ConcurrentLinkedQueue<PacketOutbound> getOutboundQueue() {
		return this.outboundqueue;
	}

	@Override
	public void offer(PacketOutbound p) {
		this.outboundqueue.offer(p);
	}

	@Override
	public PacketInbound poll() {
		return this.inboundqueue.poll();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
