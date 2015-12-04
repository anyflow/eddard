package com.lge.stark.eddard.httphandlers;

import org.junit.BeforeClass;

import com.lge.stark.eddard.ApiTestSuite;

public class UserTest {

	private static String BASE_ADDRESS;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApiTestSuite.setup();

		BASE_ADDRESS = "http://localhost:8080/user";
	}

}