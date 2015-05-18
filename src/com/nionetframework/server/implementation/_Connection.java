package com.nionetframework.server.implementation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.nionetframework.server.api.Connection;
import com.nionetframework.server.api.ConnectionManager;
import com.nionetframework.server.api.Packet;
import com.nionetframework.server.api.PacketInbound;
import com.nionetframework.server.api.Server;
import com.nionetframework.server.util.Logger;

public class _Connection implements Connection {

	private DataOutputStream out;
	private DataInputStream in;
	private final _ConnectionManager connectionmanager;
	private final ConcurrentLinkedQueue<Packet> queue;
	private ByteBuffer writebuf;
	private Packet currentwritepacket;
	private SocketChannel socketchannel;
	private ByteBuffer readbuf;
	private String address;
	private Server server;

	private int redundantLoops = 0;

	public _Connection(_ConnectionManager connectionmanager, SocketChannel s) {
		this.socketchannel = s;
		this.server = connectionmanager.getServer();
		this.connectionmanager = connectionmanager;
		this.queue = new ConcurrentLinkedQueue<Packet>();
		this.initializeStreams();
		try {
			this.address = socketchannel.getRemoteAddress().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initializeStreams() {
		// try {
		// this.out = new DataOutputStream(socketchannel..getOutputStream());
		// } catch (IOException e) {
		// Logger.error("(Connection) Failed creating DataOutputStream for socket: "
		// + socket.toString());
		// e.printStackTrace();
		// }
		// try {
		// this.in = new DataInputStream(socket.getInputStream());
		// } catch (IOException e) {
		// Logger.error("(Connection) Failed creating DataInputStream for socket: "
		// + socket.toString());
		// e.printStackTrace();
		// }
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
						Logger.fatal("(Connection) Detected impossible packetcollission: 4!");
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
					connectionmanager.getServer().getNetworkThread()
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
						((_NetworkThread) ((_Server) server).getNetworkThread())
								.queueInterestChange(new _InterestChangeEvent(
										socketchannel, SelectionKey.OP_READ));
					} else {
						((_NetworkThread) ((_Server) server).getNetworkThread())
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
						((_NetworkThread) ((_Server) server).getNetworkThread())
								.queueInterestChange(new _InterestChangeEvent(
										socketchannel, SelectionKey.OP_READ));
					} else {
						// Focus on writing.
						// ((_NetworkThread) ((_Server)
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
						// ((_NetworkThread) ((_Server)
						// server).getNetworkThread()).queueInterestChange(
						// new _InterestChangeEvent(socketchannel,
						// SelectionKey.OP_READ));
					} else {
						// ((_NetworkThread) ((_Server)
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

	@Override
	public boolean queue(Packet p) {
		boolean succes = this.queue.offer(p);
		((_NetworkThread) ((_Server) server).getNetworkThread())
				.queueInterestChange(new _InterestChangeEvent(socketchannel,
						SelectionKey.OP_WRITE));
		return succes;
	}

	public void _terminateSocketChannel() {
		try {
			this.socketchannel.close();
		} catch (IOException e) {
			Logger.warning("(Connection) Failed to close the connection manually. Is it already closed?");
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
