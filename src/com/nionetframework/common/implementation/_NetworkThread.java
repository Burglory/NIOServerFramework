package com.nionetframework.common.implementation;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.nionetframework.common.api.Connection;
import com.nionetframework.common.api.InterestChangeEvent;
import com.nionetframework.common.api.NetworkThread;
import com.nionetframework.common.api.PacketInbound;
import com.nionetframework.common.api.PacketOutbound;

public abstract class _NetworkThread implements NetworkThread {

	private boolean terminate;
	private ConcurrentLinkedQueue<InterestChangeEvent> interestchangevents;
	private ConcurrentLinkedQueue<PacketInbound> inboundQueue;
	private ConcurrentLinkedQueue<PacketOutbound> outboundQueue;

	public _NetworkThread() {
	this.terminate = false;
	this.interestchangevents = new ConcurrentLinkedQueue<InterestChangeEvent>();

	this.inboundQueue = new ConcurrentLinkedQueue<PacketInbound>();
	this.outboundQueue = new ConcurrentLinkedQueue<PacketOutbound>();
	
	}
	

	public boolean isTerminated() {
		return terminate;
	}
	
	public void terminate() {
		this.terminate = true;
	}
	
	public ConcurrentLinkedQueue<InterestChangeEvent> getInterestChangeEvents() {
		return this.interestchangevents;
	}
	
	public ConcurrentLinkedQueue<PacketInbound> getInboundQueue() {
		return inboundQueue;
	}

	public ConcurrentLinkedQueue<PacketOutbound> getOutboundQueue() {
		return outboundQueue;
	}
	
	@Override
	public void offer(PacketOutbound p) {
		for(Connection c : p.getDestinations()) {
			((_Connection) c).queue(p);
		}
	}

	@Override
	public PacketInbound poll() {
		return this.inboundQueue.poll();
	}
	
}
