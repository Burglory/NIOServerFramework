package mexica.network.events;

import java.util.Collection;

import com.burglory.generic.multiplayer.server.api.Connection;
import com.burglory.generic.multiplayer.server.implementation._Packet;

public class NetworkEventEnvelop {

	private final NetworkEvent networkevent;
	private final Collection<Connection> to;

	public NetworkEventEnvelop(NetworkEvent e, Collection<Connection> to) {
		this.networkevent = e;
				this.to = to;
	}
	
	private void send() {

	}
	
	public NetworkEvent getNetworkEvent() {
		return this.networkevent;
	}
	
	public Collection<Connection> getTo() {
		return this.to;
	}
	
}
