package mexica.network.events;

public class NetworkEventDispatcherOutbound extends NetworkEventDispatcher {
	
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
		EventsServerThread.offer(e);
	}

}
