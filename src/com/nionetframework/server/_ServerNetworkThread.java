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
import com.nionetframework.common.logger.Logger;

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
		Logger.Log("Initializing ServerNetworkThread...", Logger.MESSAGE);
		this.server = server;
		this.address = i;

		Logger.Log("NetworkManager succesfully initialized.", Logger.MESSAGE);
	}

	@Override
	public void run() {
		Logger.Log("Opening ServerSocketChannel...", Logger.MESSAGE);
		ServerSocketChannel serverChannel = null;
		try {
			serverChannel = ServerSocketChannel.open();
		} catch (IOException e1) {
			Logger.Log("Failed to open ServerSocketChannel", Logger.FATAL);
			System.exit(1);
		}
		Logger.Log("Opening ServerSocketChannel was succesful!", Logger.MESSAGE);
		try {
			serverChannel.configureBlocking(false);
		} catch (IOException e1) {
			Logger.Log(
					"Failed to set blocking to false for the ServerSocketChannel",
					Logger.FATAL);
			System.exit(1);
		}
		Logger.Log("Setting blocking to false was succesful!", Logger.MESSAGE);
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
		Logger.Log("Registration of Selector was succesful!", Logger.MESSAGE);
		Logger.Log("Binding to: " + address.toString(), Logger.MESSAGE);
		try {
			serverChannel.bind(address);
		} catch (NumberFormatException e1) {
			Logger.Log("Failed to bind to address: " + address.toString(),
					Logger.FATAL);
			System.exit(1);
		} catch (IOException e1) {
			Logger.Log("Failed to bind to address: " + address.toString(),
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
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		Logger.Log("Exiting Loop...", Logger.MESSAGE);
	}

	private void accept(SelectionKey key) {
		try {

			SocketChannel socketChannel = ((ServerSocketChannel) key.channel())
					.accept();
			Logger.Log("New connection from: "
					+ socketChannel.getRemoteAddress().toString(),
					Logger.MESSAGE);
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
	
	@Override
	public void wakeup() {
		this.selector.wakeup();
	}

}
