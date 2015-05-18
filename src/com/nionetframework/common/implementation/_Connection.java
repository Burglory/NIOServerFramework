package com.nionetframework.common.implementation;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.nionetframework.common.api.Connection;
import com.nionetframework.common.api.ConnectionManager;
import com.nionetframework.common.api.Packet;
import com.nionetframework.common.api.PacketInbound;
import com.nionetframework.common.logger.Logger;
import com.nionetframework.server.implementation._ServerNetworkThread;

public abstract class _Connection implements Connection {

	private _ConnectionManager connectionmanager;
	protected ConcurrentLinkedQueue<Packet> queue;

	private Packet currentwritepacket;
	private SocketChannel socketchannel;

	private String address;

	private ByteBuffer headerwritebuffer;
	private ByteBuffer headerreadbuffer;
	private ByteBuffer writebuffer;
	private ByteBuffer readbuffer;

	private static final int HEADER_SIZE = 4;

	private int redundantLoops = 0;
	private boolean isreadingheader;

	private boolean iswritingheader;

	public _Connection(_ConnectionManager connectionmanager, SocketChannel s) {
		this.socketchannel = s;
		this.connectionmanager = connectionmanager;
		this.queue = new ConcurrentLinkedQueue<Packet>();
		try {
			this.address = socketchannel.getRemoteAddress().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.isreadingheader = true;
		// this.iswritingheader = true;
		// readbuffer = ByteBuffer.allocate(HEADER_SIZE);
		// writebuffer = ByteBuffer.allocate(HEADER_SIZE);
		headerreadbuffer = ByteBuffer.allocate(HEADER_SIZE);
		headerwritebuffer = ByteBuffer.allocate(HEADER_SIZE);
	}

	private boolean readHeader() {
		System.out.println("Reading header...");
		int amount_read = -1;
		if (headerreadbuffer.hasRemaining()) {
			try {
				amount_read = socketchannel.read(headerreadbuffer);
			} catch (IOException e) {

				e.printStackTrace();
				this.connectionmanager.disconnect(this);
				return false;
			}
			if (amount_read == -1) {
				this.connectionmanager.disconnect(this);
				return false;
				// socketchannel.close();
			}
		}

		if (!headerreadbuffer.hasRemaining()) {
			headerreadbuffer.rewind();
			int packetsize = headerreadbuffer.getInt();
			readbuffer = ByteBuffer.allocate(packetsize);
			this.isreadingheader = false;
		}
		return true;
	}

	private boolean readBody() {
		int amount_read = -1;
		if (readbuffer.hasRemaining()) {
			try {
				amount_read = socketchannel.read(readbuffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.connectionmanager.disconnect(this);
				return false;
			}
			if (amount_read == -1) {
				this.connectionmanager.disconnect(this);
				return false;
				// socketchannel.close();
			}
		}

		if (!readbuffer.hasRemaining()) {
			readbuffer.rewind();
			byte[] packet = new byte[readbuffer.remaining()];
			readbuffer.get(packet);
			PacketInbound p = new _PacketInbound(packet, this);
			// Schedule packet for processing.
			((_ServerNetworkThread) connectionmanager.getNetworkThread())
					.getInboundQueue().offer(p);
			Logger.Log("Received message from: " + this.getAddress()
					+ " saying: " + p.getData(), Logger.DEBUG);
			// Prepare to read the next packetsize.
			headerreadbuffer.position(0).limit(HEADER_SIZE);
			this.isreadingheader = true;
		}
		return true;
	}

	public boolean _read() {
		if (this.isreadingheader) {
			return readHeader();
		} else {
			return readBody();
		}
	}

	private boolean writeHeader() {
		// Writing packetsize
		if (headerwritebuffer.hasRemaining()) {
			System.out.println("Writing the header: "
					+ this.currentwritepacket.getData());
			try {
				socketchannel.write(headerwritebuffer);
			} catch (IOException e) {
				e.printStackTrace();
				this.connectionmanager.disconnect(this);
				return false;
			}
		}
		if (!headerwritebuffer.hasRemaining()) {
			this.iswritingheader = false;
			// Prepare packet for writing!
			writebuffer = ByteBuffer.wrap(currentwritepacket.getBytes());
			// ((_ServerNetworkThread) connectionmanager.getNetworkThread())
			// .queueInterestChange(new _InterestChangeEvent(
			// socketchannel, SelectionKey.OP_READ));
		} else {
			// ((_ServerNetworkThread) connectionmanager.getNetworkThread())
			// .queueInterestChange(new _InterestChangeEvent(
			// socketchannel, SelectionKey.OP_WRITE));
		}
		return true;
	}

	private boolean writeBody() {
		if (writebuffer.hasRemaining()) {
			System.out.println("Writing the body: "
					+ this.currentwritepacket.getData());
			try {
				socketchannel.write(writebuffer);
			} catch (IOException e) {
				e.printStackTrace();
				this.connectionmanager.disconnect(this);
				return false;
			}
		}
		if (!writebuffer.hasRemaining()) {
			this.iswritingheader = true;
			// We are done with this packet!
			System.out.println("Packet succesfully written: "
					+ this.currentwritepacket.getData());
			currentwritepacket = null;
			// ((_ServerNetworkThread) connectionmanager.getNetworkThread())
			// .queueInterestChange(new _InterestChangeEvent(
			// socketchannel, SelectionKey.OP_READ));
		} else {
			// Focus on writing.
			// ((_ServerNetworkThread) ((_Server)
			// server).getNetworkThread()).queueInterestChange(
			// new _InterestChangeEvent(socketchannel,
			// SelectionKey.OP_WRITE));
		}
		return true;
	}

	public boolean _write() {
		// Works
		// System.out.println("Entering write method.");
		if (currentwritepacket == null) {
			// TODO: Nothing gets added to the queue!
			currentwritepacket = this.queue.poll();
			if (currentwritepacket == null) {
				// Done here for now!

				return true;
			} else {
				// We have a packet to write!
				this.iswritingheader = true;
				System.out.println("We need to write a packet!: "
						+ this.currentwritepacket.getData());
				headerwritebuffer.position(0).limit(HEADER_SIZE);
				headerwritebuffer.putInt(currentwritepacket.getBytes().length);
				headerwritebuffer.rewind();
				// Try to write the header right now already to decrease latency
				this.writeHeader();
			}
		}

		if (this.iswritingheader) {
			return writeHeader();
		} else {
			return writeBody();
		}

	}

	/**
	 * Queues a {@link Packet} for this {@link Connection}.
	 * 
	 * @param p
	 *            the {@link} Packet to be send.
	 */
	public boolean queue(Packet p) {
		boolean succes = this.queue.offer(p);
		// ((_ServerNetworkThread) connectionmanager.getNetworkThread())
		// .queueInterestChange(new _InterestChangeEvent(socketchannel,
		// SelectionKey.OP_WRITE));
		return succes;
	}

	public void _terminateSocketChannel() {
		try {
			this.socketchannel.close();
		} catch (IOException e) {
			Logger.Log(
					"(Connection) Failed to close the connection manually. Is it already closed?",
					Logger.WARNING);
			e.printStackTrace();
		}
	}

	@Override
	public ConnectionManager getConnectionManager() {
		return connectionmanager;
	}

	@Override
	public String getAddress() {

		return this.address;
	}

}
