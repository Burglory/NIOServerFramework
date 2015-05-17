package com.burglory.generic.multiplayer.server.implementation;

import com.burglory.generic.multiplayer.server.api.Connection;
import com.burglory.generic.multiplayer.server.api.OnlineGame;
import com.burglory.generic.multiplayer.server.api.OnlineGameLobby;
import com.burglory.generic.multiplayer.server.api.User;

public class _User implements User {

	private String username;
	private _Connection connection;
	private _OnlineGame onlinegame;
	private _OnlineGameLobby onlinegamelobby;

	public _User(_Connection c, String username) {
		this.username = username;
		this.connection = c;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public Connection getConnection() {
		return this.connection;
	}

	@Override
	public OnlineGame getOnlineGame() {
		return this.onlinegame;
	}

	public void unsetOnlineGame() {
		this.onlinegame = null;
	}

	public void setOnlineGame(_OnlineGame o) {
		this.onlinegame = o;
	}

	@Override
	public OnlineGameLobby getOnlineGameLobby() {
		return this.onlinegamelobby;
	}

	public void unsetOnlineGameLobby() {
		this.onlinegamelobby = null;
	}

	public void setOnlineGameLobby(_OnlineGameLobby o) {
		this.onlinegamelobby = o;
	}

	@Override
	public boolean isObserver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPlayer() {
		// TODO Auto-generated method stub
		return false;
	}

}
