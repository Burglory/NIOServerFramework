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
import com.nionetframework.server.api.Server;
import com.nionetframework.server.implementation._ServerNetworkThread;

public abstract class _Connection implements Connection {
	
	private final _ConnectionManager connectionmanager;
	private final ConcurrentLinkedQueue<Packet> queue;
	private ByteBuffer writebuf;
	private Packet currentwritepacket;
	private SocketChannel socketchannel;
	private ByteBuffer readbuf;
	private String address;


	private int redundantLoops = 0;

	public _Connection(_ConnectionManager connectionmanager, SocketChannel s) {
		this.socketchannel = s;

		this.connectionmanager = connectionmanager;
		this.queue = new ConcurrentLinkedQueue<Packet>();
		try {
			this.address = socketchannel.getRemoteAddress().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean _read() {
		int amount_read = -1;
		try {
			if (readbuf.limit() == 4) {
				// Packetsize buffer.
				if (readbuf.hasRemaining()) {
					amount_read = socketchannel.read(readbuf);
				}
				if (amount_read == -1) {
					socketchannel.close();
				}
				if (!readbuf.hasRemaining()) {
					readbuf.rewind();
					int packetsize = readbuf.getInt();
					readbuf = ByteBuffer.allocate(packetsize);
					if (packetsize == 4)
						Logger.Log("(Connection) Detected impossible packetcollission: 4!", Logger.ERROR);
				}
			} else {
				// Packet buffer.
				if (readbuf.hasRemaining()) {
					amount_read = socketchannel.read(readbuf);
				}
				if (amount_read == -1) {
					socketchannel.close();
				}
				if (!readbuf.hasRemaining()) {
					readbuf.rewind();
					byte[] packet = new byte[readbuf.remaining()];
					readbuf.get(packet);
					PacketInbound p = new _PacketInbound(packet, this);
					// Schedule packet for processing.
					((_ServerNetworkThread) connectionmanager.getNetworkThread())
							.getInboundQueue().offer(p);
					// Prepare to read the next packetsize.
					readbuf.position(0).limit(4);
				}
			}

		} catch (IOException e) {
			this.connectionmanager.disconnect(this);
			return false;
		}
		return true;
	}

	public boolean _write() {
		try {
			if (currentwritepacket != null) {
				if (writebuf.limit() == 4) {
					// Writing packetsize
					if (writebuf.hasRemaining()) {
						socketchannel.write(writebuf);
					}
					if (!writebuf.hasRemaining()) {
						writebuf = ByteBuffer.wrap(currentwritepacket
								.getBytes());
						((_ServerNetworkThread) connectionmanager.getNetworkThread())
								.queueInterestChange(new _InterestChangeEvent(
										socketchannel, SelectionKey.OP_READ));
					} else {
						((_ServerNetworkThread) connectionmanager.getNetworkThread())
								.queueInterestChange(new _InterestChangeEvent(
										socketchannel, SelectionKey.OP_WRITE));
					}
				} else {
					// Writing packet
					if (writebuf.hasRemaining()) {
						socketchannel.write(writebuf);
					}
					if (!writebuf.hasRemaining() && queue.isEmpty()) {
						currentwritepacket = null;
						((_ServerNetworkThread) connectionmanager.getNetworkThread())
								.queueInterestChange(new _InterestChangeEvent(
										socketchannel, SelectionKey.OP_READ));
					} else {
						// Focus on writing.
						// ((_ServerNetworkThread) ((_Server)
						// server).getNetworkThread()).queueInterestChange(
						// new _InterestChangeEvent(socketchannel,
						// SelectionKey.OP_WRITE));
					}
				}

			} else {
				if (!this.queue.isEmpty()) {
					this.currentwritepacket = this.queue.poll();
					writebuf.position(0).limit(4);
					writebuf.putInt(currentwritepacket.getBytes().length);
					writebuf.rewind();
					// Do this?
					if (writebuf.hasRemaining()) {
						socketchannel.write(writebuf);
					}
					if (!writebuf.hasRemaining()) {
						writebuf = ByteBuffer.wrap(currentwritepacket
								.getBytes());
						// ((_ServerNetworkThread) ((_Server)
						// server).getNetworkThread()).queueInterestChange(
						// new _InterestChangeEvent(socketchannel,
						// SelectionKey.OP_READ));
					} else {
						// ((_ServerNetworkThread) ((_Server)
						// server).getNetworkThread()).queueInterestChange(
						// new _InterestChangeEvent(socketchannel,
						// SelectionKey.OP_WRITE));
					}
				} else {
					redundantLoops++;
				}
			}
			if (redundantLoops > 10) {
				// Sleep?

				redundantLoops = 0;
			}
		} catch (IOException e) {
			this.connectionmanager.disconnect(this);
			// socketchannel.close();
			return false;
		}
		return true;
	}

	/**
	 * Sends a {@link Packet} through this {@link Connection}.
	 * 
	 * @param p
	 *            the {@link} Packet to be send.
	 */
	public boolean queue(Packet p) {
		boolean succes = this.queue.offer(p);
		((_ServerNetworkThread) connectionmanager.getNetworkThread())
				.queueInterestChange(new _InterestChangeEvent(socketchannel,
						SelectionKey.OP_WRITE));
		return succes;
	}

	public void _terminateSocketChannel() {
		try {
			this.socketchannel.close();
		} catch (IOException e) {
			Logger.Log("(Connection) Failed to close the connection manually. Is it already closed?", Logger.WARNING);
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
