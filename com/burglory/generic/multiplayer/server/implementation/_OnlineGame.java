package com.burglory.generic.multiplayer.server.implementation;

import java.util.Set;

import com.burglory.generic.multiplayer.server.api.OnlineGame;
import com.burglory.generic.multiplayer.server.api.OnlineGameLobby;
import com.burglory.generic.multiplayer.server.api.User;
import com.burglory.generic.multiplayer.server.util.OnlineGameStatus;

public class _OnlineGame implements OnlineGame {

	private Set<User> players;
	private _OnlineGameLobby onlinegamelobby;
	private OnlineGameStatus onlinegamestatus;

	public _OnlineGame(_OnlineGameLobby o) {
		this.onlinegamelobby = o;
		this.players = o.getPlayers();
		this.onlinegamestatus = OnlineGameStatus.Created;
		this.setGameForPlayers();
	}

	private void setGameForPlayers() {
		for (User u : players) {
			((_User) u).setOnlineGame(this);
		}
	}

	private void unSetGameForPlayers() {
		for (User u : players) {
			((_User) u).unsetOnlineGame();
		}
	}

	@Override
	public OnlineGameStatus getOnlineGameStatus() {
		return this.onlinegamestatus;
	}

	@Override
	public void setOnlineGameStatus(OnlineGameStatus o) {
		this.onlinegamestatus = o;
	}

	@Override
	public User getUser(String username) {
		for (User u : this.players) {
			if (u.getUsername().equals(username))
				return u;
		}
		return null;
	}

	@Override
	public Set<User> getPlayers() {
		return this.players;
	}

	@Override
	public boolean isStarted() {
		return this.onlinegamestatus.getProgress() >= OnlineGameStatus.Started
				.getProgress();
	}

	@Override
	public OnlineGameLobby getOnlineGameLobby() {
		return this.onlinegamelobby;
	}

}
