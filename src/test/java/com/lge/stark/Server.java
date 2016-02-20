package com.lge.stark;

import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;

import net.anyflow.menton.http.MockHttpServer;

public class Server {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Server.class);

	public final static MockHttpServer SERVER;
	public final static String BASE_URI;

	static {
		try {
			Entrypoint.loadEnvironmentalSettings();
		}
		catch (FactoryConfigurationError | IOException e) {
			logger.error(e.getMessage(), e);
		}

		SERVER = new MockHttpServer("com.lge.stark.httphandler");
		BASE_URI = "http://localhost:8080";
	}
}