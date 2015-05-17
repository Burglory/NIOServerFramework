package mexica.network.events;

import com.burglory.generic.multiplayer.server.api.User;

import mexica.core.Player;
import mexica.multiplayer.server.core.Lobby;

public abstract class PlayerLobbyEvent extends PlayerNetworkEvent {

	public static final String EVENT_NAME = PlayerNetworkEvent.EVENT_NAME + "l";
	public static final String LOBBY_FIELD = "l";
	private Lobby lobby;

	public PlayerLobbyEvent(User u, Lobby l) {
		super(u);
		this.lobby = l;
	}
	
	public Lobby getLobby() {
		return this.lobby;
	}

}
