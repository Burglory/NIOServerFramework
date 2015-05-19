package com.nionetframework.client.examples;

import java.net.InetSocketAddress;
import java.util.Scanner;

import com.nionetframework.client.Client;
import com.nionetframework.common.Packet;
import com.nionetframework.common.PacketInbound;
import com.nionetframework.common.logger.Logger;

public class ClientExample {

	public static void main(String[] args) {
		new ClientExample().loop();
	}

	private Scanner scanner;
	private Client client;

	public ClientExample() {
		Logger.setLogLevel(Logger.DEBUG);

		this.client = Client.getDefaultClient();
		client.setInetAddress(new InetSocketAddress("localhost", 8500));
		client.start();
		this.scanner = new Scanner(System.in);
		this.loop();
		Logger.Log("Starting System In Loop...", Logger.DEBUG);

	}

	private void loop() {
		while (client.getThread().isAlive()) {
			String message = scanner.nextLine();
			Logger.Log("Sending: " + message, Logger.MESSAGE);
			client.getNetworkThread().offer(new Packet(message));
			PacketInbound p = client.getNetworkThread().poll();
			if (p != null) {
				System.out.println("Server says: " + p.getData());
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
