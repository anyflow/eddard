package com.lge.stark.eddard;

import java.io.File;
import java.io.FileReader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.lge.stark.eddard.controller.room.DeviceTest;
import com.lge.stark.eddard.controller.room.RoomTest;
import com.lge.stark.eddard.mockserver.ProfileServer;

import junit.framework.TestSuite;
import net.anyflow.menton.http.MockHttpServer;

@RunWith(Suite.class)
@SuiteClasses({ RoomTest.class, DeviceTest.class })
public class ApiTestSuite extends TestSuite {

	private static MockHttpServer server = null;

	@BeforeClass
	public static void setUp() throws Exception {
		String log4jFilePath = (new File(Environment.getWorkingPath(Entrypoint.class),
				Configurator.instance().LOG4J_PROPERTIES_FILE_NAME)).getPath();

		org.apache.log4j.xml.DOMConfigurator.configure(log4jFilePath);

		String mentonPropFilePath = Environment.getWorkingPath(Entrypoint.class) + "/"
				+ Configurator.instance().APPLICATION_PROPERTIES_FILE_NAME;

		net.anyflow.menton.Configurator.instance().initialize(new FileReader(mentonPropFilePath));

		String profileDataPath = Environment.getWorkingPath(Entrypoint.class) + "/testdata/profile.json";
		ProfileServer.instance().load(profileDataPath);

		server = new MockHttpServer("com.lge.stark.eddard.httphandler");
	}

	@AfterClass
	public static void tearDown() throws Exception {
	}

	public static MockHttpServer server() {
		return server;
	}
}