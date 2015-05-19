package com.nionetframework.common.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NetworkLogger {

	public static final int DEBUG = 0;
	public static final int MESSAGE = 1;
	public static final int EVENT = 2;
	public static final int WARNING = 3;
	public static final int ERROR = 4;
	public static final int FATAL = 5;
	public static final int DEFAULT = 3;

	private static final String DEBUG_String = "DEBUG";
	private static final String MESSAGE_String = "MESSAGE";
	private static final String EVENT_String = "EVENT";
	private static final String WARNING_String = "WARNING";
	private static final String ERROR_String = "ERROR";
	private static final String FATAL_String = "FATAL";

	private static int tresholdloglevel = WARNING;

	private static String getString(int loglevel) {
		switch (loglevel) {
		case DEBUG:
			return DEBUG_String;
		case MESSAGE:
			return MESSAGE_String;
		case EVENT:
			return EVENT_String;
		case WARNING:
			return WARNING_String;
		case ERROR:
			return ERROR_String;
		case FATAL:
			return FATAL_String;
		}
		return null;
	}

	public static void Log(String message) {
		Log(message, DEBUG);
	}

	public static void Log(String message, int loglevel) {
		if (loglevel >= tresholdloglevel) {
			message = new SimpleDateFormat().format(new Date()) + ": "
					+ getString(loglevel) + ": " + message;
			System.out.println(message);
		}
	}

	public static void setLogLevel(int verbosity) {
		tresholdloglevel = verbosity;
	}

}
