package com.lge.stark.eddard.mockserver;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

import com.lge.stark.eddard.ApiTestSuite;
import com.lge.stark.eddard.model.Profile;

public class ProfileServerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApiTestSuite.setup();
	}

	@Test
	public void testGetLogined() throws Exception {
		Profile profile = ProfileServer.SELF.getLogined("37cd2cd5-0a1e-4641-9f5e-6096b16b64d5");

		assertThat(profile.getDeviceId(), is("09bab373-ff80-42ca-a221-ba3e8345a469"));
	}
}