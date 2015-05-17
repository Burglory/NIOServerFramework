package com.burglory.generic.multiplayer.server.implementation;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import mexica.multiplayer.server.utilities.SecurityUtilities;

import com.burglory.generic.multiplayer.server.api.OnlineGame;
import com.burglory.generic.multiplayer.server.api.OnlineGameLobby;
import com.burglory.generic.multiplayer.server.api.User;

public class _OnlineGameLobby implements OnlineGameLobby {

	private final String lobbyname;
	private final User admin;
	private final String password;
	private final int maxobservers;
	private final int maxplayers;
	private _OnlineGame onlinegame;
	private final Set<User> observers;
	private final Set<User> users;
	private final Set<User> players;
	private final String uuid;

	public _OnlineGameLobby(String lobbyname, User admin, String password,
			int maxobservers, int maxplayers) {
		this.lobbyname = lobbyname;
		this.admin = admin;
		this.password = password;
		this.maxobservers = maxobservers;
		this.maxplayers = maxplayers;
		this.uuid = SecurityUtilities.generateRandomToken();
		this.observers = Collections
				.newSetFromMap(new ConcurrentHashMap<User, Boolean>());
		this.users = Collections
				.newSetFromMap(new ConcurrentHashMap<User, Boolean>());
		this.players = Collections
				.newSetFromMap(new ConcurrentHashMap<User, Boolean>());
	}

	@Override
	public String getUUID() {
		return this.uuid;
	}

	@Override
	public OnlineGame getOnlineGame() {
		return this.onlinegame;
	}

	@Override
	public User getAdmin() {
		return this.admin;
	}

	@Override
	public Set<User> getObservers() {
		return this.observers;
	}

	@Override
	public Set<User> getUsers() {
		return this.users;
	}

	@Override
	public Set<User> getPlayers() {
		return this.players;
	}

	public String getLobbyname() {
		return lobbyname;
	}

	public String getPassword() {
		return password;
	}

	public int getMaxobservers() {
		return maxobservers;
	}

	public int getMaxplayers() {
		return maxplayers;
	}

	@Override
	public boolean newOnlineGame() {
		this.onlinegame = new _OnlineGame(this);
		return false;
	}

	@Override
	public boolean joinLobby(User u) {
		if (this.observers.size() < this.maxobservers + 1) {
			return false;
		} else {
			this.observers.add(u);
			this.users.add(u);
			((_User) u).setOnlineGameLobby(this);
			return true;
		}
	}

	@Override
	public boolean becomePlayer(User u) {
		if (this.players.size() < this.maxplayers + 1) {
			return false;
		} else {
			this.players.add(u);
			this.observers.remove(u);
			return true;
		}
	}

	@Override
	public boolean leaveLobby(User u) {
		if (this.users.remove(u)) {
			((_User) u).unsetOnlineGameLobby();
			if (this.players.remove(u)) {
				return true;
			}
			if (this.observers.remove(u)) {
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public boolean becomeObserver(User u) {
		// TODO Auto-generated method stub
		return false;
	}

}
