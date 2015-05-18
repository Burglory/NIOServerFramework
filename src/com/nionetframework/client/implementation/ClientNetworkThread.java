package com.nionetframework.client.implementation;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.nionetframework.common.api.Connection;
import com.nionetframework.common.api.PacketInbound;
import com.nionetframework.common.api.PacketOutbound;
import com.nionetframework.common.implementation._Connection;
import com.nionetframework.common.implementation._NetworkThread;
import com.nionetframework.common.logger.Logger;

public class ClientNetworkThread extends _NetworkThread {

	private ConcurrentLinkedQueue<PacketInbound> inboundqueue;
	private ConcurrentLinkedQueue<PacketOutbound> outboundqueue;
	private Client client;
	private String ip;
	private String port;
	private InetSocketAddress address;
	private Selector selector;

	public ClientNetworkThread(Client client, InetSocketAddress i) {
		this.inboundqueue = new ConcurrentLinkedQueue<PacketInbound>();
		this.outboundqueue = new ConcurrentLinkedQueue<PacketOutbound>();
		this.client = client;
		this.address = i;
	}

	public ConcurrentLinkedQueue<PacketInbound> getInboundQueue() {
		return this.inboundqueue;
	}

	public ConcurrentLinkedQueue<PacketOutbound> getOutboundQueue() {
		return this.outboundqueue;
	}

	@Override
	public void run() {
		selector = null;
		try {
			selector = Selector.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SocketChannel channel = null;
		try {
			channel = SocketChannel.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		try {
			channel.configureBlocking(false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		}
		try {
			channel.socket().setKeepAlive(true);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		// channel.socket().setReuseAddress(true);
		try {
			channel.socket().setSoLinger(false, 0);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		try {
			channel.socket().setSoTimeout(0);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		try {
			channel.socket().setTcpNoDelay(true);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}

		try {
			channel.connect(address);
		} catch (IOException e) {
			Logger.Log(e.getMessage(), Logger.FATAL);
			System.exit(1);
		}
		try {
			channel.register(selector, SelectionKey.OP_CONNECT);
		} catch (ClosedChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		while (!isTerminated()) {
			try {
				loop();
			} catch (Throwable t) {
				t.printStackTrace();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void loop() throws IOException {
		updateInterests(selector);


		selector.selectNow();


		Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
		// Logger.Log("(ServerThread): Current amount of selected keys: "
		// + selector.selectedKeys().size(), Logger.DEBUG);

		while (iterator.hasNext()) {
			SelectionKey key = iterator.next();
			iterator.remove();
			if (!key.isValid())
				continue;
			if (key.isConnectable()) {
				this.connect(key);
			}
			// if (key.isAcceptable()) {
			// this.accept(key);
			// } else
			if (key.isReadable()) {
				if (!((_Connection) key.attachment())._read())
					key.cancel();
			} else if (key.isWritable()) {
				if (!((_Connection) key.attachment())._write())
					key.cancel();
			}

		}
	}

	private void connect(SelectionKey key) throws IOException {
	    SocketChannel ch = (SocketChannel) key.channel();
	    if (ch.finishConnect()) {
	      Logger.Log("Connected to: " + ch.getRemoteAddress().toString(), Logger.EVENT);
//	      key.interestOps(key.interestOps() ^ SelectionKey.OP_CONNECT);
	      SelectionKey readKey = key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
			Connection newconnection = client.getConnectionManager().addConnection(
					ch);
			readKey.attach(newconnection);
	    }
	}

}
