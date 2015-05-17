package com.burglory.generic.multiplayer.server.api;

import java.util.Set;

public interface OnlineGameLobby {

	String getUUID();

	OnlineGame getOnlineGame();

	Set<User> getObservers();

	Set<User> getUsers();

	Set<User> getPlayers();

	/**
	 * Returns the {@link User} that created the OnlineGameLobby and is
	 * considered the admin of this lobby.
	 */
	User getAdmin();

	/** Creates a new OnlineGame for this OnlineGameLobby. */
	boolean newOnlineGame();

	/**
	 * Join the OnlineGameLobby as a observer. To become a player, use
	 * {@link #becomePlayer(User u)}
	 */
	boolean joinLobby(User u);

	/**
	 * Flags the {@link User} as a player, if the maximum of players is not
	 * reached.
	 */
	boolean becomePlayer(User u);

	/**
	 * Flags the {@link User} as a observer, if the maximum of observers is not
	 * reached.
	 */
	boolean becomeObserver(User u);

	/** Removes the {@link User} from the lobby. */
	boolean leaveLobby(User u);
}
