package com.burglory.generic.multiplayer.server.api;

public interface InboundPacketWorkerThread extends Runnable {

	boolean queue(Packet p);

	boolean stop();
}
