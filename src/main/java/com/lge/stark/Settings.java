package com.lge.stark;

import java.io.FileReader;
import java.io.IOException;

import io.netty.handler.logging.LogLevel;

public class Settings extends java.util.Properties {

	private static final long serialVersionUID = -3232325711649130464L;

	protected static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Settings.class);

	public final static String APPLICATION_PROPERTIES_FILE_PATH;
	public final static String LOG4J_PROPERTIES_FILE_PATH;

	public final static Settings SELF;

	static {
		APPLICATION_PROPERTIES_FILE_PATH = Environment.getWorkingPath(Entrypoint.class) + "/application.properties";
		LOG4J_PROPERTIES_FILE_PATH = Environment.getWorkingPath(Entrypoint.class) + "/log4j.xml";

		SELF = new Settings();
	}

	private Settings() {
		try {
			load(new FileReader(APPLICATION_PROPERTIES_FILE_PATH));
		}
		catch (IOException e) {
			logger.error("Settings instantiation failed.", e);
		}
	}

	public int getInt(String key, int defaultValue) {
		String valueString = this.getProperty(key.trim());

		if (valueString == null) { return defaultValue; }

		try {
			return Integer.parseInt(valueString.trim());
		}
		catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		String valueString = this.getProperty(key.trim());

		if (valueString == null) { return defaultValue; }

		return "true".equalsIgnoreCase(valueString.trim());
	}

	/**
	 * @return context root path
	 */
	public String httpContextRoot() {
		String ret = getProperty("webserver.contextRoot", "/");

		if (ret.equalsIgnoreCase("") || ret.charAt(ret.length() - 1) != '/') {
			ret += "/";
		}

		return ret;
	}

	public LogLevel logLevel() {
		if (logger.isTraceEnabled()) {
			return LogLevel.TRACE;
		}
		else if (logger.isDebugEnabled()) {
			return LogLevel.DEBUG;
		}
		else if (logger.isInfoEnabled()) {
			return LogLevel.INFO;
		}
		else if (logger.isWarnEnabled()) {
			return LogLevel.WARN;
		}
		else {
			return LogLevel.DEBUG;
		}
	}
}