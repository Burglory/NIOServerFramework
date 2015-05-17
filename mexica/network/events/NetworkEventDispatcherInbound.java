package mexica.network.events;

import java.util.LinkedList;
import java.util.Queue;

public class NetworkEventDispatcherInbound extends NetworkEventDispatcher {

	public NetworkEventDispatcherInbound() {
		super();

	}
	
	@Override
	void processEvent(NetworkEvent e) {
		for(RegisteredEventListener l : getRegisteredeventlisteners()) {
			l.dispatchEvent(e);
		}
		if(e instanceof CancellableNetworkEvent) {
			if(((CancellableNetworkEvent) e).isCancelled()) {
				return;
			}
			((CancellableNetworkEvent) e).setCancellabe(false);
		}
		e.execute();
	}
	
	

}
