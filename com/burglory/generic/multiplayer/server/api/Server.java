package com.burglory.generic.multiplayer.server.api;

public interface Server {

	ConnectionManager getConnectionManager();

	OnlineGameLobbyManager getLobbyManager();

	InboundPacketWorkerThread getPacketHandler();

	boolean stop();

	boolean start();

	boolean start(String port);

}
