package com.burglory.generic.multiplayer.server.implementation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.burglory.generic.multiplayer.server.api.Connection;
import com.burglory.generic.multiplayer.server.api.ConnectionManager;
import com.burglory.generic.multiplayer.server.api.Packet;
import com.burglory.generic.multiplayer.server.api.User;
import com.burglory.generic.multiplayer.server.util.Logger;
import com.burglory.generic.multiplayer.server.util.PacketUtil;

public class _Connection implements Connection {

	private User user;
	private DataOutputStream out;
	private DataInputStream in;
	private final _ConnectionManager connectionmanager;
	private final ConcurrentLinkedQueue<Packet> queue;
	private ByteBuffer writebuf;
	private Packet currentwritepacket;
	private SocketChannel socketchannel;
	private ByteBuffer readbuf;

	public _Connection(_ConnectionManager connectionmanager, SocketChannel s) {
		this.socketchannel = s;
		this.connectionmanager = connectionmanager;
		this.user = null;
		this.queue = new ConcurrentLinkedQueue<Packet>();
		this.initializeStreams();
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

	public boolean read() {
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
					Packet p = PacketUtil.generatePacketWrapper(this, packet);
					// Schedule packet for processing.
					connectionmanager.getServer().getPacketHandler().queue(p);
					// Prepare to read the next packetsize.
					readbuf.position(0).limit(4);
				}
			}

		} catch (IOException e) {
			try {
				socketchannel.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
		return true;
	}

	public boolean write() {
		try {
			if (currentwritepacket != null) {
				if (writebuf.limit() == 4) {
					// Writing packetsize
					if (writebuf.hasRemaining()) {
						socketchannel.write(writebuf);
					}
					if (!writebuf.hasRemaining()) {
						writebuf = ByteBuffer.wrap(PacketUtil
								.getBytes(currentwritepacket));
						((_Server) this.getConnectionManager().getServer())
								.getServerThread().queueInterestChange(
										new _InterestChangeEvent(socketchannel,
												SelectionKey.OP_READ));
					} else {
						((_Server) this.getConnectionManager().getServer())
								.getServerThread().queueInterestChange(
										new _InterestChangeEvent(socketchannel,
												SelectionKey.OP_WRITE));
					}
				} else {
					// Writing packet
					if (writebuf.hasRemaining()) {
						socketchannel.write(writebuf);
					}
					if (!writebuf.hasRemaining()) {
						currentwritepacket = null;
						((_Server) this.getConnectionManager().getServer())
								.getServerThread().queueInterestChange(
										new _InterestChangeEvent(socketchannel,
												SelectionKey.OP_READ));
					} else {
						((_Server) this.getConnectionManager().getServer())
								.getServerThread().queueInterestChange(
										new _InterestChangeEvent(socketchannel,
												SelectionKey.OP_WRITE));
					}
				}

			} else {
				if (!this.queue.isEmpty()) {
					this.currentwritepacket = this.queue.poll();
					writebuf.position(0).limit(4);
					writebuf.putInt(PacketUtil.getBytes(currentwritepacket).length);
					writebuf.rewind();
					// Do this?
					if (writebuf.hasRemaining()) {
						socketchannel.write(writebuf);
					}
					if (!writebuf.hasRemaining()) {
						writebuf = ByteBuffer.wrap(PacketUtil
								.getBytes(currentwritepacket));
						((_Server) this.getConnectionManager().getServer())
								.getServerThread().queueInterestChange(
										new _InterestChangeEvent(socketchannel,
												SelectionKey.OP_READ));
					} else {
						((_Server) this.getConnectionManager().getServer())
								.getServerThread().queueInterestChange(
										new _InterestChangeEvent(socketchannel,
												SelectionKey.OP_WRITE));
					}
				}
			}
		} catch (IOException e) {
			try {
				socketchannel.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean queue(Packet p) {
		return this.queue.offer(p);
	}

	public void stop() {
		try {
			this.socketchannel.close();
		} catch (IOException e) {
			Logger.warning("(Connection) Failed to close the connection manually. Is it already closed?");
			e.printStackTrace();
		}
	}

	public User getUser() {
		return this.user;
	}

	public boolean unsetUser() {
		this.user = null;
		return false;
	}

	public boolean setUser(String username) {
		if (this.user == null) {
			this.user = new _User(this, username);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ConnectionManager getConnectionManager() {
		return connectionmanager;
	}

}
