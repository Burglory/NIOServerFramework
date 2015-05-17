package com.nioserverframework.implementation;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.nioserverframework.api.InterestChangeEvent;
import com.nioserverframework.api.NetworkThread;
import com.nioserverframework.api.PacketInbound;
import com.nioserverframework.api.PacketOutbound;
import com.nioserverframework.logger.ServerLogger;
import com.nioserverframework.util.Logger;

public class _NetworkThread implements NetworkThread {

	private String port;
	private boolean terminate;
	private Selector selector;
	private _Server server;
	private ConcurrentLinkedQueue<InterestChangeEvent> interestchangevents;
	private final ConcurrentLinkedQueue<PacketInbound> inboundQueue;
	private final ConcurrentLinkedQueue<PacketOutbound> outboundQueue;

	public _NetworkThread(_Server server, String port) {
		ServerLogger.Log("Initializing NetworkThread...", ServerLogger.MESSAGE);
		this.server = server;
		this.port = port;
		this.terminate = false;
		this.interestchangevents = new ConcurrentLinkedQueue<InterestChangeEvent>();
		
		this.inboundQueue = new ConcurrentLinkedQueue<PacketInbound>();
		this.outboundQueue = new ConcurrentLinkedQueue<PacketOutbound>();
		ServerLogger.Log("NetworkManager succesfully initialized.", ServerLogger.MESSAGE);
	}

	@Override
	public void run() {
		ServerLogger.Log("Opening ServerSocketChannel...", ServerLogger.MESSAGE);
		ServerSocketChannel serverChannel = null;
		try {
			serverChannel = ServerSocketChannel.open();
		} catch (IOException e1) {
			ServerLogger.Log("Failed to open ServerSocketChannel", ServerLogger.FATAL);
			System.exit(1);
		}
		ServerLogger.Log("Opening ServerSocketChannel was succesful!", ServerLogger.MESSAGE);
		try {
			serverChannel.configureBlocking(false);
		} catch (IOException e1) {
			ServerLogger.Log("Failed to set blocking to false for the ServerSocketChannel", ServerLogger.FATAL);
			System.exit(1);
		}
		ServerLogger.Log("Setting blocking to false was succesful!", ServerLogger.MESSAGE);
		ServerLogger.Log("Opening Selector...", ServerLogger.MESSAGE);
		try {
			selector = Selector.open();
		} catch (IOException e1) {
			ServerLogger.Log("Failed to open Selector", ServerLogger.FATAL);
			System.exit(1);
		}
		ServerLogger.Log("Opened Selector succesfully!", ServerLogger.MESSAGE);
		try {
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e1) {
			ServerLogger.Log("Failed to register Selector for the ServerSocketChannel", ServerLogger.FATAL);
			System.exit(1);
		}
		ServerLogger.Log("Registration of Selector was succesful!", ServerLogger.MESSAGE);
		ServerLogger.Log("Binding to: " + "localhost:" + port, ServerLogger.MESSAGE);
		try {
			serverChannel.bind(new InetSocketAddress("localhost", Integer
					.parseInt(port)));
		} catch (NumberFormatException e1) {
			ServerLogger.Log("Failed to bind to address: " + "localhost", ServerLogger.FATAL);
			System.exit(1);
		} catch (IOException e1) {
			ServerLogger.Log("Failed to bind to address: " + "localhost", ServerLogger.FATAL);
			System.exit(1);
		}
		ServerLogger.Log("Binding was succesful!", ServerLogger.MESSAGE);
		
		ServerLogger.Log("Starting Loop...", ServerLogger.MESSAGE);
		while (!terminate) {
			try {
				loop();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		ServerLogger.Log("Exiting Loop...", ServerLogger.MESSAGE);
	}

	private void updateInterests() {
		while (this.interestchangevents.size() > 0) {
			InterestChangeEvent ice = this.interestchangevents.poll();
			SelectionKey key = ice.getSocket().keyFor(selector);
			key.interestOps(ice.getInterests());
		}
	}

	private void accept(SelectionKey key) {
		try {
			SocketChannel socketChannel = ((ServerSocketChannel) key.channel())
					.accept();
			socketChannel.configureBlocking(false);
			SelectionKey readKey = socketChannel.register(selector,
					SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			readKey.attach(server.getConnectionManager().addConnection(
					socketChannel));

			Logger.info("New connection from: "
					+ socketChannel.getRemoteAddress().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void loop() throws Throwable {
		updateInterests();

		selector.selectNow();

		Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
		//ServerLogger.Log("(ServerThread): Current amount of selected keys: " + selector.selectedKeys().size(), ServerLogger.DEBUG);

		while (iterator.hasNext()) {
			SelectionKey key = iterator.next();
			iterator.remove();
			if (!key.isValid())
				continue;

			if (key.isAcceptable()) {
				this.accept(key);
			} else if (key.isReadable()) {
				if (!((_Connection) key.attachment())._read())
					key.cancel();
			} else if (key.isWritable()) {
				if (!((_Connection) key.attachment())._write())
					key.cancel();
			}

		}

	}

	public boolean queueInterestChange(InterestChangeEvent e) {
		return this.interestchangevents.offer(e);
	}

	public void terminate() {
		this.terminate = true;
	}

	public ConcurrentLinkedQueue<PacketInbound> getInboundQueue() {
		return inboundQueue;
	}

	public ConcurrentLinkedQueue<PacketOutbound> getOutboundQueue() {
		return outboundQueue;
	}

}
