package com.burglory.generic.multiplayer.server.api;

public interface User {

	String getUsername();

	Connection getConnection();

	OnlineGame getOnlineGame();

	OnlineGameLobby getOnlineGameLobby();

	boolean isObserver();

	boolean isPlayer();

}
