package com.nionetframework.client.examples;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.nionetframework.client.Client;
import com.nionetframework.common.Packet;
import com.nionetframework.common.PacketInbound;
import com.nionetframework.common.logger.NetworkLogger;

public class ClientExample {

	public static void main(String[] args) {
		new ClientExample().loop();
	}

	private Scanner scanner;
	private Client client;

	public ClientExample() {
		NetworkLogger.setLogLevel(NetworkLogger.DEBUG);

		this.client = Client.getDefaultClient();
		client.setInetAddress(new InetSocketAddress("localhost", 8500));
		client.start();
		this.scanner = new Scanner(System.in);
		this.loop();
		NetworkLogger.Log("Starting System In Loop...", NetworkLogger.DEBUG);

	}

	private void loop() {
		while (client.getThread().isAlive()) {
			String message = scanner.nextLine();
			NetworkLogger.Log("Sending: " + message, NetworkLogger.MESSAGE);
			client.getNetworkThread().offer(new Packet(message.getBytes(StandardCharsets.UTF_8)));
			PacketInbound p = client.getNetworkThread().poll();
			if (p != null) {
				System.out.println("Server says: " + new String(p.getBytes(), StandardCharsets.UTF_8));
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
