package com.nionetframework.server.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.nionetframework.common.logger.Logger;

public class RegisteredServerEventListener {

	private HashMap<Class<? extends ServerEvent>, Collection<Method>> map;
	private ServerEventListener eventlistener;

	public RegisteredServerEventListener(ServerEventListener l) {
		this.eventlistener = l;
		this.map = new HashMap<Class<? extends ServerEvent>, Collection<Method>>();
		this.registerMethods();
	}

	public void unregisterMethods() {
		for (Method m : getAllEventMethods(eventlistener.getClass())) {
			Class<? extends ServerEvent> eventclass = getEventClassForMethod(m);
			if (map.containsKey(eventclass)) {
				map.get(eventclass).remove(m);
			}

		}
	}

	public void registerMethods() {
		Collection<Method> alleventmethods = getAllEventMethods(eventlistener
				.getClass());
		for (Method m : alleventmethods) {
			Class<? extends ServerEvent> eventclass = getEventClassForMethod(m);
			if (!map.containsKey(eventclass)) {
				map.put(eventclass, new ArrayList<Method>());
			}
			map.get(eventclass).add(m);

		}
	}

	private Class<? extends ServerEvent> getEventClassForMethod(Method m) {
		Class<?>[] types = m.getParameterTypes();
		if (types.length == 1) {
			try {
				Logger.Log("Returning EventClass: " + types[0].getName());
				if (ServerEvent.class.isAssignableFrom(types[0])) {
					return (Class<? extends ServerEvent>) types[0];
				} else {
					throw new RuntimeException("Class: " + types[0].getName()
							+ " is not an instance of ServerEvent.class!");
				}

			} catch (ClassCastException e) {
				throw new RuntimeException("Class: " + types[0].getName()
						+ " is not an instance of ServerEvent.class!");
			}
		}
		return null;
	}

	private Collection<Method> getAllEventMethods(Class<?> c) {
		Collection<Method> result = new ArrayList<Method>();
		for (Method m : c.getDeclaredMethods()) {
			if (m.getAnnotation(ServerEvents.class) != null) {
				Logger.Log("Found an ServerEvent Method: " + m.getName());
				result.add(m);
			}
		}
		return result;
	}

	public void dispatchEvent(ServerEvent e) {
		if (!map.containsKey(e.getClass())) {
			map.put(e.getClass(), new ArrayList<Method>());
		}
		for (Method m : map.get(e.getClass())) {
			try {
				Logger.Log("Dispatching ServerEvent for Method: "
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

	public ServerEventListener getEventListener() {
		return this.eventlistener;
	}

}
