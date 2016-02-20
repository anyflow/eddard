package com.lge.stark.mockserver;

import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lge.stark.mockserver.UserServer;
import com.lge.stark.model.User;

public class UserServerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testGet() throws Exception {
		User user = UserServer.SELF.get("9658b662-9352-46d5-b778-908bc90204a6");

		Assert.assertThat(user, CoreMatchers.notNullValue());
	}
}
