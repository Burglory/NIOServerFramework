package mexica.network.events;

import java.util.LinkedList;
import java.util.Queue;

import mexica.network.Packet;

import com.burglory.generic.multiplayer.server.api.Connection;
import com.burglory.generic.multiplayer.server.api.Server;

public class EventsServerThread implements Runnable {

	private Server server;
	private boolean terminate;
	private static final Queue<NetworkEventEnvelop> outqueue =new LinkedList<NetworkEventEnvelop>();

	public EventsServerThread(Server s) {
		this.server = s;
		this.terminate = false;
	}
	
	public void terminate() {
		this.terminate = true;
	}
	
	@Override
	public void run() {
		while(!terminate) {
			NetworkEventEnvelop e = outqueue.poll();
			if(e!=null) {
				for(Connection c : e.getTo()) {
					c.queuePacket(new Packet(e.getNetworkEvent()));
				}
			}
		}
	}

	public static void offer(NetworkEventEnvelop e) {
		outqueue.offer(e);
	}

}
