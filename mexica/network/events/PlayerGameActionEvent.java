package mexica.network.events;

import com.burglory.generic.multiplayer.server.api.User;

import mexica.core.Player;
import mexica.core.events.PlayerEvent;

public abstract class PlayerGameActionEvent extends PlayerNetworkEvent {

	public static final String EVENT_NAME = PlayerNetworkEvent.EVENT_NAME + "ga";
	public static final String PLAYEREVENT_FIELD = "e";
	private PlayerEvent event;

	public PlayerGameActionEvent(User u, PlayerEvent e) {
		super(u);
		this.event = e;
	}
	
	public PlayerEvent getPlayerAction() {
		return this.event;
	}

}
