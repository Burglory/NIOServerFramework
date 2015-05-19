package com.nionetframework.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.nionetframework.common.Connection;
import com.nionetframework.common._Connection;
import com.nionetframework.common._NetworkThread;
import com.nionetframework.common.logger.NetworkLogger;

class _ServerNetworkThread extends _NetworkThread implements
		ServerNetworkThread {

	// private boolean terminate;
	private Selector selector;
	private Server server;
	private InetSocketAddress address;

	// private ConcurrentLinkedQueue<InterestChangeEvent> interestchangevents;
	// private final ConcurrentLinkedQueue<PacketInbound> inboundQueue;
	// private final ConcurrentLinkedQueue<PacketOutbound> outboundQueue;

	_ServerNetworkThread(Server server, InetSocketAddress i) {
		super();
		NetworkLogger.Log("Initializing ServerNetworkThread...", NetworkLogger.MESSAGE);
		this.server = server;
		this.address = i;

		NetworkLogger.Log("NetworkManager succesfully initialized.", NetworkLogger.MESSAGE);
	}

	@Override
	public void run() {
		NetworkLogger.Log("Opening ServerSocketChannel...", NetworkLogger.MESSAGE);
		ServerSocketChannel serverChannel = null;
		try {
			serverChannel = ServerSocketChannel.open();
		} catch (IOException e1) {
			NetworkLogger.Log("Failed to open ServerSocketChannel", NetworkLogger.FATAL);
			System.exit(1);
		}
		NetworkLogger.Log("Opening ServerSocketChannel was succesful!", NetworkLogger.MESSAGE);
		try {
			serverChannel.configureBlocking(false);
		} catch (IOException e1) {
			NetworkLogger.Log(
					"Failed to set blocking to false for the ServerSocketChannel",
					NetworkLogger.FATAL);
			System.exit(1);
		}
		NetworkLogger.Log("Setting blocking to false was succesful!", NetworkLogger.MESSAGE);
		NetworkLogger.Log("Opening Selector...", NetworkLogger.MESSAGE);
		try {
			selector = Selector.open();
		} catch (IOException e1) {
			NetworkLogger.Log("Failed to open Selector", NetworkLogger.FATAL);
			System.exit(1);
		}
		NetworkLogger.Log("Opened Selector succesfully!", NetworkLogger.MESSAGE);
		try {
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e1) {
			NetworkLogger.Log(
					"Failed to register Selector for the ServerSocketChannel",
					NetworkLogger.FATAL);
			System.exit(1);
		}
		NetworkLogger.Log("Registration of Selector was succesful!", NetworkLogger.MESSAGE);
		NetworkLogger.Log("Binding to: " + address.toString(), NetworkLogger.MESSAGE);
		try {
			serverChannel.bind(address);
		} catch (NumberFormatException e1) {
			NetworkLogger.Log("Failed to bind to address: " + address.toString(),
					NetworkLogger.FATAL);
			System.exit(1);
		} catch (IOException e1) {
			NetworkLogger.Log("Failed to bind to address: " + address.toString(),
					NetworkLogger.FATAL);
			System.exit(1);
		}
		NetworkLogger.Log("Binding was succesful!", NetworkLogger.MESSAGE);

		NetworkLogger.Log("Starting Loop...", NetworkLogger.MESSAGE);
		while (!isTerminated()) {
			try {
				loop();
			} catch (Throwable t) {
				t.printStackTrace();
			}
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		NetworkLogger.Log("Exiting Loop...", NetworkLogger.MESSAGE);
	}

	private void accept(SelectionKey key) {
		try {

			SocketChannel socketChannel = ((ServerSocketChannel) key.channel())
					.accept();
			NetworkLogger.Log("New connection from: "
					+ socketChannel.getRemoteAddress().toString(),
					NetworkLogger.MESSAGE);
			socketChannel.configureBlocking(false);
			SelectionKey readKey = socketChannel.register(selector,
					SelectionKey.OP_READ);
			Connection newconnection = server.getConnectionManager()
					.addConnection(socketChannel);
			readKey.attach(newconnection);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void loop() throws Throwable {
		updateInterests(selector);

//		selector.selectNow();
		selector.select();
		System.out.println("Selecting keys...");

		Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
		// NetworkLogger.Log("(ServerThread): Current amount of selected keys: "
		// + selector.selectedKeys().size(), NetworkLogger.DEBUG);

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
	
	@Override
	public void wakeup() {
		this.selector.wakeup();
	}

}
