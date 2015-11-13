package com.lge.stark.eddard;

import java.io.FileReader;
import java.io.IOException;

import io.netty.handler.logging.LogLevel;

public class Configurator extends java.util.Properties {

	private static final long serialVersionUID = -3232325711649130464L;

	protected static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Configurator.class);

	public final String APPLICATION_PROPERTIES_FILE_NAME = "application.properties";
	public final String LOG4J_PROPERTIES_FILE_NAME = "log4j.xml";

	private static Configurator instance;

	private Configurator() {
	}

	public static void instantiateWith(String appPropFilePath) {
		instance = new Configurator();

		if(appPropFilePath == null) { return; }

		try {
			instance.load(new FileReader(appPropFilePath));
		}
		catch(IOException e) {
			logger.error("Configurator instantiation failed.", e);
		}
	}

	public static Configurator instance() {
		if(instance == null) {
			instance = new Configurator();

			try {
				String appPropFilePath = Environment.getWorkingPath(Entrypoint.class) + "/" + instance.APPLICATION_PROPERTIES_FILE_NAME;

				instance.load(new FileReader(appPropFilePath));
			}
			catch(IOException e) {
				logger.error("Configurator instantiation failed.", e);
				return null;
			}
		}

		return instance;
	}

	public int getInt(String key, int defaultValue) {
		String valueString = this.getProperty(key.trim());

		if(valueString == null) { return defaultValue; }

		try {
			return Integer.parseInt(valueString.trim());
		}
		catch(NumberFormatException e) {
			return defaultValue;
		}
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		String valueString = this.getProperty(key.trim());

		if(valueString == null) { return defaultValue; }

		return "true".equalsIgnoreCase(valueString.trim());
	}

	/**
	 * @return context root path
	 */
	public String httpContextRoot() {
		String ret = getProperty("webserver.contextRoot", "/");

		if(ret.equalsIgnoreCase("") || ret.charAt(ret.length() - 1) != '/') {
			ret += "/";
		}

		return ret;
	}

	public LogLevel logLevel() {
		if(logger.isTraceEnabled()) {
			return LogLevel.TRACE;
		}
		else if(logger.isDebugEnabled()) {
			return LogLevel.DEBUG;
		}
		else if(logger.isInfoEnabled()) {
			return LogLevel.INFO;
		}
		else if(logger.isWarnEnabled()) {
			return LogLevel.WARN;
		}
		else {
			return LogLevel.DEBUG;
		}
	}
}