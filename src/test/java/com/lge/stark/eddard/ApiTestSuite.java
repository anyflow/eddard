package com.lge.stark.eddard;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.lge.stark.eddard.controller.room.DeviceTest;
import com.lge.stark.eddard.controller.room.RoomTest;

import junit.framework.TestSuite;
import net.anyflow.menton.http.MockHttpServer;

@RunWith(Suite.class)
@SuiteClasses({ RoomTest.class, DeviceTest.class })
public class ApiTestSuite extends TestSuite {

	private static MockHttpServer server = null;

	@BeforeClass
	public static void setUp() throws Exception {
		Entrypoint.initialize();

		server = new MockHttpServer("com.lge.stark.eddard.httphandler");
	}

	@AfterClass
	public static void tearDown() throws Exception {
	}

	public static MockHttpServer server() {
		return server;
	}
}