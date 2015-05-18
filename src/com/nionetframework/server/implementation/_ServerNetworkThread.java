package com.nionetframework.server.implementation;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.nionetframework.common.api.Connection;
import com.nionetframework.common.api.InterestChangeEvent;
import com.nionetframework.common.api.PacketInbound;
import com.nionetframework.common.api.PacketOutbound;
import com.nionetframework.common.implementation._Connection;
import com.nionetframework.common.implementation._NetworkThread;
import com.nionetframework.common.logger.Logger;
import com.nionetframework.server.api.ServerNetworkThread;

public class _ServerNetworkThread extends _NetworkThread implements ServerNetworkThread {

	private String port;
//	private boolean terminate;
	private Selector selector;
	private _Server server;
//	private ConcurrentLinkedQueue<InterestChangeEvent> interestchangevents;
//	private final ConcurrentLinkedQueue<PacketInbound> inboundQueue;
//	private final ConcurrentLinkedQueue<PacketOutbound> outboundQueue;

	public _ServerNetworkThread(_Server server, String port) {
		Logger.Log("Initializing ServerNetworkThread...", Logger.MESSAGE);
		this.server = server;
		this.port = port;

		Logger.Log("NetworkManager succesfully initialized.",
				Logger.MESSAGE);
	}

	@Override
	public void run() {
		Logger
				.Log("Opening ServerSocketChannel...", Logger.MESSAGE);
		ServerSocketChannel serverChannel = null;
		try {
			serverChannel = ServerSocketChannel.open();
		} catch (IOException e1) {
			Logger.Log("Failed to open ServerSocketChannel",
					Logger.FATAL);
			System.exit(1);
		}
		Logger.Log("Opening ServerSocketChannel was succesful!",
				Logger.MESSAGE);
		try {
			serverChannel.configureBlocking(false);
		} catch (IOException e1) {
			Logger
					.Log("Failed to set blocking to false for the ServerSocketChannel",
							Logger.FATAL);
			System.exit(1);
		}
		Logger.Log("Setting blocking to false was succesful!",
				Logger.MESSAGE);
		Logger.Log("Opening Selector...", Logger.MESSAGE);
		try {
			selector = Selector.open();
		} catch (IOException e1) {
			Logger.Log("Failed to open Selector", Logger.FATAL);
			System.exit(1);
		}
		Logger.Log("Opened Selector succesfully!", Logger.MESSAGE);
		try {
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e1) {
			Logger.Log(
					"Failed to register Selector for the ServerSocketChannel",
					Logger.FATAL);
			System.exit(1);
		}
		Logger.Log("Registration of Selector was succesful!",
				Logger.MESSAGE);
		Logger.Log("Binding to: " + "localhost:" + port,
				Logger.MESSAGE);
		try {
			serverChannel.bind(new InetSocketAddress("localhost", Integer
					.parseInt(port)));
		} catch (NumberFormatException e1) {
			Logger.Log("Failed to bind to address: " + "localhost",
					Logger.FATAL);
			System.exit(1);
		} catch (IOException e1) {
			Logger.Log("Failed to bind to address: " + "localhost",
					Logger.FATAL);
			System.exit(1);
		}
		Logger.Log("Binding was succesful!", Logger.MESSAGE);

		Logger.Log("Starting Loop...", Logger.MESSAGE);
		while (!isTerminated()) {
			try {
				loop();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		Logger.Log("Exiting Loop...", Logger.MESSAGE);
	}

	private void updateInterests() {
		while (this.getInterestChangeEvents().size() > 0) {
			InterestChangeEvent ice = this.getInterestChangeEvents().poll();
			SelectionKey key = ice.getSocket().keyFor(selector);
			key.interestOps(ice.getInterests());
		}
	}

	private void accept(SelectionKey key) {
		try {
			
			SocketChannel socketChannel = ((ServerSocketChannel) key.channel())
					.accept();
			Logger.Log("New connection from: "
					+ socketChannel.getRemoteAddress().toString(), Logger.MESSAGE);
			socketChannel.configureBlocking(false);
			SelectionKey readKey = socketChannel.register(selector,
					SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			Connection newconnection = server.getConnectionManager().addConnection(
					socketChannel);
			readKey.attach(newconnection);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void loop() throws Throwable {
		updateInterests();

		selector.selectNow();

		Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
		// Logger.Log("(ServerThread): Current amount of selected keys: "
		// + selector.selectedKeys().size(), Logger.DEBUG);

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
		return this.getInterestChangeEvents().offer(e);
	}

	@Override
	public void offer(PacketOutbound p) {
		for(Connection c : p.getDestinations()) {
			((_Connection) c).queue(p);
		}
	}

	@Override
	public PacketInbound poll() {
		return this.getInboundQueue().poll();
	}

}