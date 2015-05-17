package com.nioserverframework.util;

public class Logger {

	private enum LogLevel {
		DEBUG(0), INFO(1), WARNING(2), ERROR(3), FATAL(4);

		private int value;

		private LogLevel(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}

	private static final LogLevel loglevel = LogLevel.DEBUG;

	public static void debug(String string) {
		if (loglevel.getValue() > LogLevel.DEBUG.getValue())
			return;
		System.out.println("DEBUG: " + string);
	}

	public static void info(String string) {
		if (loglevel.getValue() > LogLevel.INFO.getValue())
			return;
		System.out.println("INFO: " + string);
	}

	public static void warning(String string) {
		if (loglevel.getValue() > LogLevel.WARNING.getValue())
			return;
		System.out.println("WARNING: " + string);
	}

	public static void error(String string) {
		if (loglevel.getValue() > LogLevel.ERROR.getValue())
			return;
		System.out.println("ERROR: " + string);
	}

	public static void fatal(String string) {
		if (loglevel.getValue() > LogLevel.FATAL.getValue())
			return;
		System.out.println("FATAL: " + string);
	}

}
