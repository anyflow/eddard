package com.lge.stark.eddard;

import java.io.File;

import net.anyflow.menton.http.HttpServer;

public class Entrypoint {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Entrypoint.class);
	private static Entrypoint instance;

	public static Entrypoint instance() {
		if(instance == null) {
			instance = new Entrypoint();
		}

		return instance;
	}

	public static void main(String[] args) {
		Thread.currentThread().setName("main thread");
		if(!Entrypoint.instance().start()) {
			System.exit(-1);
		}
	}

	private final HttpServer httpServer = new HttpServer();

	public boolean start() {
		try {
			String log4jFilePath = (new File(Environment.getWorkingPath(Entrypoint.class), Configurator.instance().LOG4J_PROPERTIES_FILE_NAME))
					.getPath();
			org.apache.log4j.xml.DOMConfigurator.configure(log4jFilePath);

			logger.info("Starting Eddard...");

			httpServer.start("com.lge.stark.eddard.httphandler");

			Runtime.getRuntime().addShutdownHook(new Thread() {

				@Override
				public void run() {
					shutdown(true);
				}
			});
		}
		catch(Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}

		logger.info("Eddard started...");
		return true;
	}

	public void shutdown(boolean haltJavaRuntime) {
		logger.info("Eddard shutting down...");

		try {
			httpServer.shutdown();
		}
		catch(Exception e) {
			logger.debug(e.getMessage(), e);
		}

		Entrypoint.instance = null;

		logger.info("Eddard shutdowned gracefully.");
	}
}
