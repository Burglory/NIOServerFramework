package com.nionetframework.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.nionetframework.common.Connection;
import com.nionetframework.common.Packet;
import com.nionetframework.common.PacketInbound;
import com.nionetframework.common.PacketOutbound;
import com.nionetframework.common._Connection;
import com.nionetframework.common._NetworkThread;
import com.nionetframework.common.logger.Logger;

class _ClientNetworkThread extends _NetworkThread implements
		ClientNetworkThread {

	private ConcurrentLinkedQueue<PacketInbound> inboundqueue;
	private ConcurrentLinkedQueue<PacketOutbound> outboundqueue;
	private _Client _Client;
	private InetSocketAddress address;
	private Selector selector;

	_ClientNetworkThread(_Client _Client, InetSocketAddress address) {
		this.inboundqueue = new ConcurrentLinkedQueue<PacketInbound>();
		this.outboundqueue = new ConcurrentLinkedQueue<PacketOutbound>();
		this._Client = _Client;
		this.address = address;
	}

	public ConcurrentLinkedQueue<PacketInbound> getInboundQueue() {
		return this.inboundqueue;
	}

	public ConcurrentLinkedQueue<PacketOutbound> getOutboundQueue() {
		return this.outboundqueue;
	}

	@Override
	public void run() {
		try {
			selector = null;
			selector = Selector.open();
			SocketChannel channel = null;
			channel = SocketChannel.open();
			channel.configureBlocking(false);
			channel.socket().setKeepAlive(true);
			channel.socket().setSoLinger(false, 0);
			channel.socket().setSoTimeout(0);
			channel.socket().setTcpNoDelay(true);
			channel.connect(address);
			channel.register(selector, SelectionKey.OP_CONNECT);
		} catch (SocketException e) {
			Logger.Log(
					"Running ClientNetworkThread Failed. Reason: "
							+ e.getMessage(), Logger.FATAL);
		} catch (ClosedChannelException e) {
			Logger.Log(
					"Running ClientNetworkThread Failed. Reason: "
							+ e.getMessage(), Logger.FATAL);
		} catch (IOException e) {
			Logger.Log(
					"Running ClientNetworkThread Failed. Reason: "
							+ e.getMessage(), Logger.FATAL);
		}

		while (!isTerminated()) {
			try {
				loop();
			} catch (Throwable t) {
				t.printStackTrace();
			}
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				Logger.Log(
//						"ClientNetworkThread has been interrupted while sleeping. Reason: "
//								+ e.getMessage(), Logger.WARNING);
//			}
		}
	}

	private void loop() throws IOException {
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
			if (key.isConnectable()) {
				if (!this.connect(key)) {
					System.exit(1);
				}
			}
			if (key.isReadable()) {
				if (!((_Connection) key.attachment())._read())
					key.cancel();
			} else if (key.isWritable()) {
				if (!((_Connection) key.attachment())._write())
					key.cancel();
			}
		}
	}

	private boolean connect(SelectionKey key) {
		SocketChannel ch = (SocketChannel) key.channel();
		try {
			if (ch.finishConnect()) {
				Logger.Log("Connected to: " + ch.getRemoteAddress().toString(),
						Logger.EVENT);
				// key.interestOps(key.interestOps() ^ SelectionKey.OP_CONNECT);
				SelectionKey readKey = key.interestOps(SelectionKey.OP_READ);
				Connection newconnection = _Client.getConnectionManager()
						.addConnection(ch);
				readKey.attach(newconnection);
				return true;
			}
		} catch (IOException e) {
			Logger.Log("Failed to connect to Server on: "
					+ this.address.toString() + "	Reason: " + e.getMessage());
		}
		return false;
	}

	@Override
	public void offer(Packet p) {
		((_Connection) ((ServerConnectionManager) this._Client
				.getConnectionManager()).getServerConnection()).queue(p);
	}

	@Override
	public void wakeup() {
		this.selector.wakeup();
	}

}
