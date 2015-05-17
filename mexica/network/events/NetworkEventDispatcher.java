package mexica.network.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import mexica.core.Game;
import mexica.core.utilities.GameLogger;

public abstract class NetworkEventDispatcher {

	private final Collection<RegisteredEventListener> registeredeventlisteners;
	private final LinkedList<NetworkEvent> queue;

	protected NetworkEventDispatcher() {
		this.registeredeventlisteners = new ArrayList<RegisteredEventListener>();
		this.queue = new LinkedList<NetworkEvent>();
	}

	public void registerListener(NetworkEventListener l) {
		Game.gameLogger.Log("Registering Listener: " + l.getClass().getName(), GameLogger.DEBUG);
		this.getRegisteredeventlisteners().add(new RegisteredEventListener(l));
	}

	public void unregisterListener(NetworkEventListener l) {
		for(RegisteredEventListener r : getRegisteredeventlisteners()) {
			if(r.getEventListener().equals(l)) {
				r.unregisterMethods();
				getRegisteredeventlisteners().remove(r);
			}
		}
	}
	
	public NetworkEvent poll() {
		return this.queue.poll();
	}
	
	public void callEvent(NetworkEvent e) {
		this.queue.offer(e);
	}
	
	abstract void processEvent(NetworkEvent e);

	public Collection<RegisteredEventListener> getRegisteredeventlisteners() {
		return registeredeventlisteners;
	}
	
}
