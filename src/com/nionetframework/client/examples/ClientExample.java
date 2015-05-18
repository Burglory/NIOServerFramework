package com.nionetframework.client.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import com.nionetframework.client.implementation.Client;
import com.nionetframework.client.implementation.ServerConnection;
import com.nionetframework.client.implementation.ServerConnectionManager;
import com.nionetframework.common.Packets;
import com.nionetframework.common.api.Connection;
import com.nionetframework.common.api.PacketInbound;
import com.nionetframework.common.api.PacketOutbound;
import com.nionetframework.common.logger.Logger;
import com.nionetframework.server.util.ServerDefaults;

public class ClientExample {

	public static void main(String[] args) {
		new ClientExample().loop();
	}

	private Scanner scanner;
	private Client client;
	
	public ClientExample() {
		Logger.setLogLevel(Logger.DEBUG);
		
		this.client = Client.getDefaultClient();
		client.start(ServerDefaults.IP, ServerDefaults.PORT);
		this.scanner = new Scanner(System.in);
		this.loop();
		Logger.Log("Starting System In Loop...", Logger.DEBUG);

	}

	private void loop() {
		while(true) {
			String message = scanner.nextLine();
			Logger.Log("Sending: " + message, Logger.MESSAGE);
			System.out.println(((ServerConnectionManager) client.getConnectionManager()).getServerConnection());
			 client.getNetworkThread().offer(Packets.generateOutboundPacket(message, Arrays.asList(((ServerConnectionManager) client.getConnectionManager()).getServerConnection())));
			System.out.println("Offered Packet to NetworkThread: " +((ServerConnection) ((ServerConnectionManager) client.getConnectionManager()).getServerConnection()).getQueue().size());
			PacketInbound p = client.getNetworkThread().poll();
			if(p!=null) {
				System.out.println("Server says: " + p.getData());
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
