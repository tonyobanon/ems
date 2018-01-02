package com.ce.ems.base.core;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Logger {

	private static final String defaultNamespace = "System.log";

	public static Boolean isSystemOtputEnabled = false;

	private static Boolean isSnapshotEnabled = false;
	private static Long snapshotInterval;
	private static LoggerInterceptor loggerInterceptor;

	private static LogPipeline logPipeline = null;
	private static PrintStream outputStream = null;

	private static Collection<String> logEntries = Collections.synchronizedList(new ArrayList<String>());

	private static boolean isInfoEnabled;
	private static boolean isWarnEnabled;
	private static boolean isErrorEnabled;
	private static boolean isFatalEnabled;
	private static boolean isDebugEnabled;
	private static boolean isTraceEnabled;

	public static void enableSystemOutput() {
		isSystemOtputEnabled = true;
	}

	private static void resetModes() {

		isInfoEnabled = false;
		isWarnEnabled = false;
		isErrorEnabled = false;
		isFatalEnabled = false;
		isDebugEnabled = false;
		isTraceEnabled = false;
	}

	public static void verboseMode(String verboseLevel) {

		Logger.verboseLevel level = null;

		try {
			level = Logger.verboseLevel.valueOf(verboseLevel);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}

		resetModes();

		switch (level) {
		case OFF:
			break;

		case TRACE:
			isInfoEnabled = true;
			isWarnEnabled = true;
			isErrorEnabled = true;
			isFatalEnabled = true;
			isDebugEnabled = true;
			isTraceEnabled = true;
			break;

		case DEBUG:
			isInfoEnabled = true;
			isWarnEnabled = true;
			isErrorEnabled = true;
			isFatalEnabled = true;
			isDebugEnabled = true;

			break;

		case FATAL:

			isInfoEnabled = true;
			isWarnEnabled = true;
			isErrorEnabled = true;
			isFatalEnabled = true;
			break;

		case ERROR:
			isInfoEnabled = true;
			isWarnEnabled = true;
			isErrorEnabled = true;
			break;

		case WARN:
			isInfoEnabled = true;
			isWarnEnabled = true;
			break;

		case INFO:
			isInfoEnabled = true;
			break;

		default:
			throw new IllegalArgumentException("Unrecognized verbose level");
		}
	}

	public static PrintStream getPrintStream() {
		return outputStream;
	}

	public static void withStream(PrintStream stream) {
		outputStream = stream;
	}

	public static void withPipeline(LogPipeline pipeline) {
		logPipeline = pipeline;
	}

	public static void withSnapshotEnabled(Boolean isEnabled) {
		isSnapshotEnabled = isEnabled;
	}

	public static void withSnapshotInterval(Long interval) {
		Logger.snapshotInterval = interval;
	}

	public static void withLoggerInterceptor(LoggerInterceptor interceptor) {
		loggerInterceptor = interceptor;
	}

	public static Boolean isSnapshotEnabled() {
		return isSnapshotEnabled;
	}

	protected static void start() {

		if (isSnapshotEnabled) {

			SingleThreadExecutor.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {

					// Send Log Entries
					loggerInterceptor.takeSnapshot(logEntries);

					// Clear Log Entries
					logEntries.clear();

				}
			}, 60, snapshotInterval, TimeUnit.SECONDS);

		}

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				// Close output stream
				if (outputStream != null) {
					outputStream.close();
				}
			}
		}));
	}

	private static boolean isDebugEnabled() {
		return isDebugEnabled;
	}

	private static boolean isErrorEnabled() {
		return isErrorEnabled;
	}

	private static boolean isFatalEnabled() {
		return isFatalEnabled;
	}

	private static boolean isInfoEnabled() {
		return isInfoEnabled;
	}

	private static boolean isTraceEnabled() {
		return isTraceEnabled;
	}

	private static boolean isWarnEnabled() {
		return isWarnEnabled;
	}

	private static void logDelegate(verboseLevel level, String namespace, String message) {
		SingleThreadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String[] lines = format(namespace, message, level);

				if (outputStream != null) {
					for (String line : lines) {
						outputStream.println(line);
					}
				}

				if (isSystemOtputEnabled) {
					for (String line : lines) {
						System.out.println(line);
					}
				}

				if (logPipeline != null) {
					for (String line : lines) {
						logPipeline.println(line);
					}
				}

				if (isSnapshotEnabled) {
					for (String line : lines) {
						logEntries.add(line);
					}
				}
			}
		});
	}

	public static void debug(String msg) {
		debug(defaultNamespace, msg);
	}

	public static void debug(String namespace, String msg) {
		if (isDebugEnabled()) {
			logDelegate(verboseLevel.DEBUG, namespace, msg);
		}
	}

	public static void error(String msg) {
		error(defaultNamespace, msg);
	}

	public static void error(String namespace, String msg) {
		if (isErrorEnabled()) {
			logDelegate(verboseLevel.ERROR, namespace, msg);
		}
	}

	public static void fatal(String msg) {
		fatal(defaultNamespace, msg);
	}

	public static void fatal(String namespace, String msg) {
		if (isFatalEnabled()) {
			logDelegate(verboseLevel.FATAL, namespace, msg);
		}
	}

	public static void info(String msg) {
		info(defaultNamespace, msg);
	}

	public static void info(String namespace, String msg) {
		if (isInfoEnabled()) {
			logDelegate(verboseLevel.INFO, namespace, msg);
		}
	}

	public static void trace(String msg) {
		trace(defaultNamespace, msg);
	}

	public static void trace(String namespace, String msg) {
		if (isTraceEnabled()) {
			logDelegate(verboseLevel.TRACE, namespace, msg);
		}
	}

	public static void warn(String msg) {
		warn(defaultNamespace, msg);
	}

	public static void warn(String namespace, String msg) {
		if (isWarnEnabled()) {
			logDelegate(verboseLevel.WARN, namespace, msg);
		}
	}

	private static String[] format(String namespace, String msg, verboseLevel level) {

		String[] lines = msg.split("\\n");

		String[] result = new String[lines.length];
		result[0] = "[" + new Date().toString() + "]" + " " + "[" + level.name() + "]" + " " + "[" + namespace + "]"
				+ " " + "[" + lines[0] + "]";

		if (lines.length > 1) {
			for (int i = 1; i < lines.length; i++) {
				result[i] = "  " + lines[i];
			}
		}
		return result;
	}

	public static enum verboseLevel {
		OFF, INFO, WARN, ERROR, FATAL, DEBUG, TRACE
	}

	static {
		verboseMode(verboseLevel.TRACE.toString());
	}
}
