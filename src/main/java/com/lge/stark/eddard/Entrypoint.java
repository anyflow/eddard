package com.lge.stark.eddard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;

import com.lge.stark.eddard.gateway.PushGateway;
import com.lge.stark.eddard.mockserver.DeviceServer;
import com.lge.stark.eddard.mockserver.ProfileServer;
import com.lge.stark.eddard.mockserver.UserServer;

import net.anyflow.menton.http.HttpServer;

public class Entrypoint {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Entrypoint.class);
	private final static Entrypoint SELF;

	static {
		SELF = new Entrypoint();
	}

	public static void main(String[] args) {
		Thread.currentThread().setName("main thread");

		if (Entrypoint.SELF.start() == false) {
			System.exit(-1);
		}
	}

	private final HttpServer httpServer = new HttpServer();

	public boolean start() {
		try {
			loadEnvironmentalSettings();

			logger.info("Starting Eddard...");

			httpServer.start("com.lge.stark.eddard.httphandler");

			PushGateway.SELF.start();

			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					shutdown(true);
				}
			});
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}

		logger.info("Eddard started...");
		return true;
	}

	public static void loadEnvironmentalSettings()
			throws FactoryConfigurationError, IOException, FileNotFoundException {

		org.apache.log4j.xml.DOMConfigurator.configure(Settings.LOG4J_PROPERTIES_FILE_PATH);

		net.anyflow.menton.Settings.SELF.initialize(new FileReader(Settings.APPLICATION_PROPERTIES_FILE_PATH));

		DeviceServer.SELF.load(Environment.getWorkingPath(Entrypoint.class) + "/testdata/device.json");
		UserServer.SELF.load(Environment.getWorkingPath(Entrypoint.class) + "/testdata/user.json");
		ProfileServer.SELF.load(Environment.getWorkingPath(Entrypoint.class) + "/testdata/profile.json");
	}

	public void shutdown(boolean haltJavaRuntime) {
		logger.info("Eddard shutting down...");

		PushGateway.SELF.shutdown();

		try {
			httpServer.shutdown();
		}
		catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}

		logger.info("Eddard shutdowned gracefully.");
	}
}