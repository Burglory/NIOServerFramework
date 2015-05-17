package com.burglory.generic.multiplayer.server.api;

import java.util.Set;

public interface OnlineGameLobbyManager {

	Set<OnlineGameLobby> getOnlineGameLobbies();

	OnlineGameLobby getOnlineGameLobby(String uuid);

	boolean addLobby(String lobbyname, User admin, String password,
			int maxobservers, int maxplayers);

	boolean removeLobby(String uuid);

}
