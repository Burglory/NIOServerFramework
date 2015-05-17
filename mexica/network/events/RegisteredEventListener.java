package mexica.network.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import mexica.core.Game;
import mexica.core.events.Event;
import mexica.core.events.Events;
import mexica.core.events.EventListener;

public class RegisteredEventListener {

	private HashMap<Class<? extends NetworkEvent>, Collection<Method>> map;
	private NetworkEventListener eventlistener;

	public RegisteredEventListener(NetworkEventListener l) {
		this.eventlistener = l;
		this.map = new HashMap<Class<? extends NetworkEvent>, Collection<Method>>();
		this.registerMethods();
	}

	public void unregisterMethods() {
		for (Method m : getAllEventMethods(eventlistener.getClass())) {
			Class<? extends NetworkEvent> eventclass = getEventClassForMethod(m);
			if (map.containsKey(eventclass)) {
				map.get(eventclass).remove(m);
			}

		}
	}

	public void registerMethods() {
		Collection<Method> alleventmethods = getAllEventMethods(eventlistener
				.getClass());
		for (Method m : alleventmethods) {
			Class<? extends NetworkEvent> eventclass = getEventClassForMethod(m);
			if (!map.containsKey(eventclass)) {
				map.put(eventclass, new ArrayList<Method>());
			}
			map.get(eventclass).add(m);

		}
	}

	private Class<? extends NetworkEvent> getEventClassForMethod(Method m) {
		Class<?>[] types = m.getParameterTypes();
		if (types.length == 1) {
			try {
				Game.gameLogger.Log("Returning EventClass: "
						+ types[0].getName());
				if (NetworkEvent.class.isAssignableFrom(types[0])) {
					return (Class<? extends NetworkEvent>) types[0];
				} else {
					throw new RuntimeException("Class: " + types[0].getName()
							+ " is not an instance of Event.class!");
				}

			} catch (ClassCastException e) {
				throw new RuntimeException("Class: " + types[0].getName()
						+ " is not an instance of Event.class!");
			}
		}
		return null;
	}

	private Collection<Method> getAllEventMethods(Class<?> c) {
		Collection<Method> result = new ArrayList<Method>();
		for (Method m : c.getDeclaredMethods()) {
			if (m.getAnnotation(Events.class) != null) {
				Game.gameLogger.Log("Found an Event Method: "
						+ m.getName());
				result.add(m);
			}
		}
		return result;
	}

	public void dispatchEvent(NetworkEvent e) {
		if (!map.containsKey(e.getClass())) {
			map.put(e.getClass(), new ArrayList<Method>());
		}
		for (Method m : map.get(e.getClass())) {
			try {
				Game.gameLogger.Log("Dispatching Event for Method: "
						+ m.getName() + " in Class: "
						+ m.getDeclaringClass().getName());
				m.invoke(eventlistener, e);
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public NetworkEventListener getEventListener() {
		return this.eventlistener;
	}

}
