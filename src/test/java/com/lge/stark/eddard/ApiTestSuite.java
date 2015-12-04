package com.lge.stark.eddard;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.lge.stark.eddard.httphandlers.DeviceTest;
import com.lge.stark.eddard.httphandlers.RoomTest;
import com.lge.stark.eddard.mockserver.ProfileServerTest;

import junit.framework.TestSuite;
import net.anyflow.menton.http.MockHttpServer;

@RunWith(Suite.class)
@SuiteClasses({ RoomTest.class, DeviceTest.class, ProfileServerTest.class })
public class ApiTestSuite extends TestSuite {

	private static MockHttpServer server = null;

	private static boolean setupLoaded = false;

	@BeforeClass
	public static void setup() throws Exception {
		if (setupLoaded) { return; }
		setupLoaded = true;

		Entrypoint.loadEnvironmentalSettings();

		server = new MockHttpServer("com.lge.stark.eddard.httphandler");
	}

	@AfterClass
	public static void tearDown() throws Exception {
	}

	public static MockHttpServer server() {
		return server;
	}
}