package com.burglory.generic.multiplayer.server.implementation;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.burglory.generic.multiplayer.server.api.InterestChangeEvent;
import com.burglory.generic.multiplayer.server.util.Logger;

public class _ServerThread implements Runnable {

	private String port;
	private boolean stop;
	private Selector selector;
	private _Server server;
	private Queue<InterestChangeEvent> interestchangevents;

	public _ServerThread(_Server server, String port) {
		this.server = server;
		this.port = port;
		this.stop = false;
		this.interestchangevents = new ConcurrentLinkedQueue<InterestChangeEvent>();

	}

	@Override
	public void run() {
		try {
			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			selector = Selector.open();
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			serverChannel.bind(new InetSocketAddress("localhost", Integer
					.parseInt(port)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (!stop) {
			try {
				loop();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
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
		Logger.debug("(ServerThread) Current amount of selected keys: "
				+ selector.selectedKeys().size());

		while (iterator.hasNext()) {
			SelectionKey key = iterator.next();
			iterator.remove();
			if (!key.isValid())
				continue;

			if (key.isAcceptable()) {
				this.accept(key);
			} else if (key.isReadable()) {
				if (!((_Connection) key.attachment()).read())
					key.cancel();
			} else if (key.isWritable()) {
				if (!((_Connection) key.attachment()).write())
					key.cancel();
			}

		}

	}

	public boolean queueInterestChange(InterestChangeEvent e) {
		return this.interestchangevents.offer(e);
	}

	public void stop() {
		this.stop = true;
	}

}
