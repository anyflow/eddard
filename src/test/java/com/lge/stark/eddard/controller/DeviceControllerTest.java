package com.lge.stark.eddard.controller;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.lge.stark.eddard.model.Device;
import com.lge.stark.eddard.model.PushType;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeviceControllerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test1Create() throws Exception {
		DeviceController.SELF.create("09bab373-ff80-42ca-a221-ba3e8345a469", "testreceiverId", PushType.APNS, false);
	}

	@Test
	public void test2UpdateStatus() throws Exception {
		DeviceController.SELF.updateStatus("09bab373-ff80-42ca-a221-ba3e8345a469", true);
	}

	@Test
	public void test4Delete() throws Exception {
		DeviceController.SELF.delete("09bab373-ff80-42ca-a221-ba3e8345a469");
	}

	@Test
	public void test3Get() throws Exception {
		List<Device> devices = DeviceController.SELF.get("09bab373-ff80-42ca-a221-ba3e8345a469");

		Assert.assertThat(devices, CoreMatchers.notNullValue());
		Assert.assertThat(devices.size(), Matchers.greaterThan(0));
		Assert.assertThat(devices.get(0), CoreMatchers.notNullValue());
	}

}
