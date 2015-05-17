package com.burglory.generic.multiplayer.server.api;

import java.util.Set;

import com.burglory.generic.multiplayer.server.util.OnlineGameStatus;

public interface OnlineGame {

	/** Returns the {@link OnlineGameStatus} this OnlineGame is in. */
	OnlineGameStatus getOnlineGameStatus();

	/**
	 * Convenience method. Usually faster than
	 * {@link ConnectionManager#getConnection(String)}, because of a smaller
	 * Set.
	 */
	User getUser(String username);

	Set<User> getPlayers();

	/** Returns wether this OnlineGame has started. */
	@Deprecated
	boolean isStarted();

	/** Returns the {@link OnlineGameLobby} associated with this OnlineGame. */
	OnlineGameLobby getOnlineGameLobby();

	/** Sets the {@link OnlineGameStatus}. */
	void setOnlineGameStatus(OnlineGameStatus o);

}
