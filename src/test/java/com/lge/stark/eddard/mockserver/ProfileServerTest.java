package com.lge.stark.eddard.mockserver;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lge.stark.eddard.Entrypoint;

public class ProfileServerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Entrypoint.loadEnvironmentalSettings();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testGetDeviceIds() throws Exception {
		List<String> deviceIds = ProfileServer.SELF.getDeviceIds("37cd2cd5-0a1e-4641-9f5e-6096b16b64d5");

		assertThat(deviceIds.size(), is(2));
	}

	@Test
	public void testGetUserIds() throws Exception {
		List<String> userIds = ProfileServer.SELF.getUserIds("d77f9eba-bf56-4d2a-b66a-b5e2987250d2");

		assertThat(userIds.size(), is(2));
	}
}
