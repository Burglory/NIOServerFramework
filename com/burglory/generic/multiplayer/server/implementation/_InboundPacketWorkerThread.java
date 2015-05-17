package com.burglory.generic.multiplayer.server.implementation;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.burglory.generic.multiplayer.server.api.InboundPacketWorkerThread;
import com.burglory.generic.multiplayer.server.api.Packet;

public class _InboundPacketWorkerThread implements InboundPacketWorkerThread {

	private _Server server;
	private ConcurrentLinkedQueue<Packet> queue;
	private boolean keepRunning;

	public _InboundPacketWorkerThread(_Server server) {
		this.server = server;
		this.queue = new ConcurrentLinkedQueue<Packet>();
		this.keepRunning = true;
	}

	@Override
	public boolean queue(Packet p) {
		return this.queue.offer(p);
	}

	@Override
	public void run() {
		while (keepRunning) {
			while (!this.queue.isEmpty()) {
				Packet toprocess = this.queue.poll();
			}
		}
	}

	@Override
	public boolean stop() {
		this.keepRunning = false;
		return true;
	}

}
