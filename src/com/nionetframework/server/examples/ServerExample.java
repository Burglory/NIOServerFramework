package com.nionetframework.server.examples;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.nionetframework.common.Connection;
import com.nionetframework.common.Packet;
import com.nionetframework.common.PacketInbound;
import com.nionetframework.common.PacketOutbound;
import com.nionetframework.common._ConnectionManager;
import com.nionetframework.common.logger.NetworkLogger;
import com.nionetframework.server.Server;
import com.nionetframework.server.events.ConnectionNewEvent;
import com.nionetframework.server.events.ServerEventDispatcher;
import com.nionetframework.server.events.ServerEventListener;
import com.nionetframework.server.events.ServerEvents;

public class ServerExample implements ServerEventListener {

	public static void main(String[] args) {
		NetworkLogger.setLogLevel(NetworkLogger.DEBUG);
		ServerExample s = new ServerExample();
	}

	private Server server;
	private Scanner scanner;
	private Connection connection;

	public ServerExample() {
		ServerEventDispatcher.registerListener(this);
		this.server = Server.getDefaultServer();
		server.start();
		this.scanner = new Scanner(System.in);
		this.loop();
		NetworkLogger.Log("Starting System In Loop...", NetworkLogger.DEBUG);

	}
	
	@ServerEvents
	public void onNewConnectionEvent(ConnectionNewEvent e) {
		this.connection = e.getConnection();
	}

	private void loop() {
		while (server.getThread().isAlive()) {
			if(connection==null) continue;
			String message = scanner.nextLine();
			NetworkLogger.Log("Sending: " + message, NetworkLogger.MESSAGE);
			server.getNetworkThread().offer(new PacketOutbound(message.getBytes(StandardCharsets.UTF_8), connection));
			PacketInbound p = server.getNetworkThread().poll();
			if (p != null) {
				System.out.println("Client says: " + new String(p.getBytes(), StandardCharsets.UTF_8));
			}
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}

}
