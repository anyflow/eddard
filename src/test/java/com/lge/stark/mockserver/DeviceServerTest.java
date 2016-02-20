package com.lge.stark.mockserver;

import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lge.stark.mockserver.DeviceServer;

public class DeviceServerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testIsValid() throws Exception {
		boolean isValid = DeviceServer.SELF.isValid("d77f9eba-bf56-4d2a-b66a-b5e2987250d2");

		Assert.assertThat(isValid, CoreMatchers.is(true));
	}

}
