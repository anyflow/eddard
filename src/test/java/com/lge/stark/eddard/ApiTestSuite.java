package com.lge.stark.eddard;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.lge.stark.eddard.controller.room.DeviceTest;
import com.lge.stark.eddard.controller.room.RoomTest;

import junit.framework.TestSuite;

@RunWith(Suite.class)
@SuiteClasses({ RoomTest.class, DeviceTest.class })
public class ApiTestSuite extends TestSuite {

	private static com.lge.stark.eddard.Entrypoint server = null;

	@BeforeClass
	public static void setUp() throws Exception {
		server = new com.lge.stark.eddard.Entrypoint();
		server.start();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		if (server == null) { return; }

		server.shutdown(false);
	}

	public static com.lge.stark.eddard.Entrypoint server() {
		return server;
	}
}