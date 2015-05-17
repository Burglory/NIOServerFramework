package mexica.network.events;

import com.burglory.generic.multiplayer.server.api.Server;

import mexica.core.utilities.DataString;
import mexica.network.events.outbound.PlayerGameActionPerformEvent;

public abstract class NetworkEvent {
	
	public static final String EVENTNAME_FIELD = "n";
	private Server server;
	
	public NetworkEvent(Server s) {
		this.server = s;
	}
	
	public Server getServer() {
		return this.server;
	}
	
	public abstract void execute();

	public abstract String getString();
	
}
