package mexica.network.events.relay;

import com.burglory.generic.multiplayer.server.api.User;
import com.burglory.generic.multiplayer.server.util.data.DataBuilder;

import mexica.core.Player;
import mexica.core.utilities.DataString;
import mexica.multiplayer.server.core.Lobby;
import mexica.network.events.NetworkEvent;
import mexica.network.events.PlayerGameActionEvent;
import mexica.network.events.PlayerLobbyEvent;
import mexica.network.events.PlayerNetworkEvent;
import mexica.network.events.outbound.PlayerGameActionPerformEvent;

public class PlayerLobbyJoinEvent extends PlayerLobbyEvent {
	
	public static final String EVENTNAME = PlayerLobbyEvent.EVENT_NAME + "j";

	public PlayerLobbyJoinEvent(User u, Lobby l) {
		super(u, l);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getString() {
		return new DataBuilder().add(NetworkEvent.EVENTNAME_FIELD)
				.wrap(EVENTNAME)
				.add(PlayerNetworkEvent.USER_FIELD)
				.wrap(getUser().getUsername())
				.add(PlayerLobbyEvent.LOBBY_FIELD)
				.wrap(getLobby().getID())
				.getString();
	}

	
	public static PlayerGameActionPerformEvent readFromString(DataString d) {
		// TODO Auto-generated method stub
		return null;
	}

}
