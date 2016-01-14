package com.lge.stark.eddard.httphandler.user;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lge.stark.eddard.Server;
import com.lge.stark.eddard.controller.DeviceController;
import com.lge.stark.eddard.model.Device;

import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.anyflow.menton.http.HttpConstants.HeaderValues;
import net.anyflow.menton.http.HttpResponse;
import net.anyflow.menton.http.IHttpClient;
import net.anyflow.menton.http.MockHttpClient;

public class PutLogoutTest {

	private static String USER_ID;
	private static String DEVICE_ID;
	private static String DEVICE_ID2 = "f5455162-0004-433c-8e62-d0b75ccbc428";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DEVICE_ID = (new com.lge.stark.eddard.httphandler.device.PostTest()).registedDeviceId();

		(new com.lge.stark.eddard.httphandler.device.PostTest()).regist(DEVICE_ID2);

		USER_ID = (new com.lge.stark.eddard.httphandler.user.PutLoginTest()).loginedUserId(DEVICE_ID);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (DEVICE_ID != null) {
			(new com.lge.stark.eddard.httphandler.device.DeleteTest()).delete(DEVICE_ID);
		}
		if (DEVICE_ID2 != null) {
			(new com.lge.stark.eddard.httphandler.device.DeleteTest()).delete(DEVICE_ID2);
		}
	}

	@Test
	public void testService() throws Exception {
		IHttpClient client = new MockHttpClient(Server.SERVER, Server.BASE_URI + "/user/" + USER_ID + "/logout");

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);

		HttpResponse response = client.put();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));

		List<Device> devices = DeviceController.SELF.get(DEVICE_ID, DEVICE_ID2);

		assertThat(devices.size(), is(2));
	}
}
