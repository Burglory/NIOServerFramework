package mexica.network;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import mexica.network.events.NetworkEvent;

public class Packet {
	
	public static final Charset charset = StandardCharsets.UTF_8;

	private String data;

	public Packet(String data) {
		this.data = data;
	}
	
	public Packet(NetworkEvent n) {
		this.data = n.getString();
	}
	
	public byte[] getBytes() {
		return this.data.getBytes(charset);
	}
	
}
