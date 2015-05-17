package com.nioserverframework.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.nioserverframework.api.NetworkThread;
import com.nioserverframework.logger.ServerLogger;

public class ServerEventDispatcher {

	private static final Collection<RegisteredServerEventListener> registeredeventlisteners = new ArrayList<RegisteredServerEventListener>();

	/**Registers all methods in a {@link ServerEventListener} that have the {@link ServerEvents} Annotation.*/
	public static void registerListener(ServerEventListener l) {
		ServerLogger.Log("Registering Listener: " + l.getClass().getName(), ServerLogger.DEBUG);
		getRegisteredeventlisteners().add(new RegisteredServerEventListener(l));
	}

	/**Unregisters all methods in a {@link ServerEventListener} that have the {@link ServerEvents} Annotation.*/
	public static void unregisterListener(ServerEventListener l) {
		for(RegisteredServerEventListener r : getRegisteredeventlisteners()) {
			if(r.getEventListener().equals(l)) {
				r.unregisterMethods();
				getRegisteredeventlisteners().remove(r);
			}
		}
	}
	
	/**Method used by the {@link NetworkThread} to call a {@link ServerEvent}.*/
	public static void callEvent(ServerEvent e) {
		for(RegisteredServerEventListener l : getRegisteredeventlisteners()) {
			l.dispatchEvent(e);
		}
//		if(e instanceof CancellableServerEvent) {
//			if(((CancellableServerEvent) e).isCancelled()) {
//				return;
//			}
//			((CancellableServerEvent) e).setCancellabe(false);
//		}
	}

	public static Collection<RegisteredServerEventListener> getRegisteredeventlisteners() {
		return registeredeventlisteners;
	}
	
}
