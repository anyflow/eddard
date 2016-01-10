package com.lge.stark.eddard.controller;

import org.hamcrest.CoreMatchers;
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
		DeviceController.SELF.create("testdeviceId", "testreceiverId", PushType.APNS, false);
	}

	@Test
	public void test2UpdateStatus() throws Exception {
		DeviceController.SELF.updateStatus("testdeviceId", true);
	}

	@Test
	public void test4Delete() throws Exception {
		DeviceController.SELF.delete("testdeviceId");
	}

	@Test
	public void test3Get() throws Exception {
		Device device = DeviceController.SELF.get("testdeviceId");

		Assert.assertThat(device, CoreMatchers.notNullValue());
	}

}
