package com.nionetframework.common;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.concurrent.ConcurrentLinkedQueue;

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

	public boolean queueInterestChange(InterestChangeEvent e) {
		boolean succes = this.getInterestChangeEvents().offer(e);
		this.wakeup();
		return succes;
	}

	public void updateInterests(Selector selector) {
		while (this.getInterestChangeEvents().size() > 0) {
			InterestChangeEvent ice = this.getInterestChangeEvents().poll();
			SelectionKey key = ice.getSocket().keyFor(selector);
			key.interestOps(ice.getInterests());
		}
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

	@Deprecated
	public ConcurrentLinkedQueue<PacketOutbound> getOutboundQueue() {
		return outboundQueue;
	}
	
	@Override
	public abstract void wakeup();

	@Override
	public void offer(PacketOutbound p) {
		System.out.println("Starting offering...");
		Connection d = null;
		for (Connection c : p.getDestinations()) {
			d=c;
			System.out.println("Offered packet to: " + c.getAddress());
			System.out.println("Accepted offer: " + ((_Connection) c).queue(p));
		}
	}

	@Override
	public PacketInbound poll() {
		return this.inboundQueue.poll();
	}

}
