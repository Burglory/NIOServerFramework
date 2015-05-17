package mexica.network.events;

import com.burglory.generic.multiplayer.server.api.Server;

public class NetworkEventProcessor implements Runnable {

	private Server server;
	private boolean terminate;
	private final NetworkEventDispatcherInbound inbound;
	private final NetworkEventDispatcherOutbound outbound;

	public NetworkEventProcessor(Server s) {
		this.server = s;
		this.terminate = false;
		this.inbound = new NetworkEventDispatcherInbound();
		this.outbound = new NetworkEventDispatcherOutbound();
	}
	
	public void terminate() {
		this.terminate = true;
	}

	@Override
	public void run() {
		while(!terminate) {
			NetworkEvent n = inbound.poll();
			if(n!=null) {
				inbound.processEvent(n);
			}
			n = outbound.poll();
			if(n!=null) {
				outbound.processEvent(n);
			}
		}
	}
	
}
