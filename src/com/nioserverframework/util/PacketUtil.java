package com.nioserverframework.util;

import java.nio.ByteBuffer;

import com.nioserverframework.api.Packet;
import com.nioserverframework.implementation._Connection;
import com.nioserverframework.implementation._Packet;

public class PacketUtil {

	/**
	 * Generates a {@link Packet} object from the {@code byte[]}. A byte[]
	 * consists of the type of packet (int) the amount of bytes of data (int)
	 * and the data (byte[]).
	 */
//	public static Packet generatePacketWrapper(_Connection source, byte[] packet) {
//		ByteBuffer bb = ByteBuffer.wrap(packet);
//		int packettype = bb.getInt();
//		int datalength = bb.getInt();
//		byte[] data = new byte[datalength];
//		bb.get(data);
//		return new _Packet(source, packettype, new String(data));
//	}

//	public static boolean isReponse(Packet request, Packet response) {
//		if (request.getConnection() == response.getConnection())
//			return false;
//		if (request.getType() != response.getType() * -1)
//			return false;
//		if (request.getData().length() != response.getData().length())
//			return false;
//		if (request.getData() != response.getData())
//			return false;
//		return true;
//	}
//
//	public static byte[] getBytes(Packet packet) {
//		byte[] data = packet.getData().getBytes();
//		return ByteBuffer.allocate(4 + 4 + data.length)
//				.putInt(packet.getType()).putInt(data.length).put(data).array();
//	}

//	@Deprecated
//	public static int getTypeFromPacket(byte[] packet) {
//		return ByteBuffer.wrap(packet).getInt();
//	}
//
//	@Deprecated
//	public static byte[] getDataFromPacket(byte[] packet) {
//		ByteBuffer packetbuffer = ByteBuffer.wrap(packet);
//		packetbuffer.getInt();
//		int datalength = packetbuffer.getInt();
//		byte[] data = new byte[datalength];
//		packetbuffer.get(data);
//		return data;
//	}

}
