package com.burglory.generic.multiplayer.server.implementation;

import com.burglory.generic.multiplayer.server.api.ConnectionManager;
import com.burglory.generic.multiplayer.server.api.InboundPacketWorkerThread;
import com.burglory.generic.multiplayer.server.api.OnlineGameLobbyManager;
import com.burglory.generic.multiplayer.server.api.Server;
import com.burglory.generic.multiplayer.server.util.ServerDefaults;

public class _Server implements Server {

	private ConnectionManager connectionmanager;
	private OnlineGameLobbyManager lobbymanager;
	private InboundPacketWorkerThread packethandler;
	private boolean stop;
	private _ServerThread serverthread;

	public _Server() {
		this.connectionmanager = new _ConnectionManager(this);
		this.lobbymanager = new _OnlineGameLobbyManager(this);
		this.packethandler = new _InboundPacketWorkerThread(this);
		this.stop = false;
	}

	@Override
	public ConnectionManager getConnectionManager() {
		return this.connectionmanager;
	}

	@Override
	public OnlineGameLobbyManager getLobbyManager() {
		return this.lobbymanager;
	}

	@Override
	public InboundPacketWorkerThread getPacketHandler() {
		return this.packethandler;
	}

	@Override
	public boolean stop() {
		this.stop = true;
		this.serverthread.stop();
		return false;
	}

	@Override
	public boolean start() {
		this.serverthread = new _ServerThread(this, ServerDefaults.PORT);
		return false;
	}

	@Override
	public boolean start(String port) {
		this.serverthread = new _ServerThread(this, port);
		return false;
	}

	public _ServerThread getServerThread() {
		return this.serverthread;
	}
}
