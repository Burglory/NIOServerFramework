package mexica.network.events;

import com.burglory.generic.multiplayer.server.api.User;

import mexica.core.Player;

public abstract class PlayerNetworkEvent extends NetworkEvent{

	public static final String EVENT_NAME = "p";
	public static final String USER_FIELD = "u";
	private User user;

	public PlayerNetworkEvent(User u) {
		this.user = u;
	}
	
	public User getUser() {
		return this.user;
	}
	
}
