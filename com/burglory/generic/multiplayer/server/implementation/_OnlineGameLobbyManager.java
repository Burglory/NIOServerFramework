package com.burglory.generic.multiplayer.server.implementation;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.burglory.generic.multiplayer.server.api.OnlineGameLobby;
import com.burglory.generic.multiplayer.server.api.OnlineGameLobbyManager;
import com.burglory.generic.multiplayer.server.api.User;

public class _OnlineGameLobbyManager implements OnlineGameLobbyManager {

	private _Server _server;
	private Set<OnlineGameLobby> onlinegamelobbies;

	public _OnlineGameLobbyManager(_Server _server) {
		this._server = _server;
		this.onlinegamelobbies = Collections
				.newSetFromMap(new ConcurrentHashMap<OnlineGameLobby, Boolean>());
	}

	@Override
	public Set<OnlineGameLobby> getOnlineGameLobbies() {
		// TODO Auto-generated method stub
		return this.onlinegamelobbies;
	}

	@Override
	public OnlineGameLobby getOnlineGameLobby(String uuid) {
		for (OnlineGameLobby o : this.onlinegamelobbies) {
			if (o.getUUID().equals(uuid))
				return o;
		}
		return null;
	}

	@Override
	public boolean addLobby(String lobbyname, User admin, String password,
			int maxobservers, int maxplayers) {
		this.onlinegamelobbies.add(new _OnlineGameLobby(lobbyname, admin,
				password, maxobservers, maxplayers));
		return false;
	}

	@Override
	public boolean removeLobby(String uuid) {
		// TODO Auto-generated method stub
		return false;
	}

}
