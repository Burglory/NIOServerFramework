package mexica.network.events.outbound;

import com.burglory.generic.multiplayer.server.api.Server;
import com.burglory.generic.multiplayer.server.api.User;
import com.burglory.generic.multiplayer.server.util.data.DataBuilder;

import mexica.core.Player;
import mexica.core.events.PlayerEvent;
import mexica.core.utilities.DataString;
import mexica.multiplayer.core.CommStandards;
import mexica.network.events.NetworkEvent;
import mexica.network.events.PlayerGameActionEvent;

public class PlayerGameActionPerformEvent extends PlayerGameActionEvent {

	public static final String EVENT_NAME = PlayerGameActionEvent.EVENT_NAME
			+ "p";

	public PlayerGameActionPerformEvent(User u, PlayerEvent e) {
		super(u, e);
	}

	public static PlayerGameActionPerformEvent readFromString(Server server, String s) {
		DataString d  = new DataString(s);
		return new PlayerGameActionPerformEvent(server.getConnectionManager().getConnection(d.get(USER_FIELD)), PlayerEvent.readFromString(d.get(PLAYEREVENT_FIELD)));
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getString() {
		return new DataBuilder().add(NetworkEvent.EVENTNAME_FIELD)
				.wrap(EVENT_NAME)
				.add(PlayerGameActionEvent.PLAYEREVENT_FIELD)
				.wrap(getPlayerAction().getString())
				.getString();
	}

}
