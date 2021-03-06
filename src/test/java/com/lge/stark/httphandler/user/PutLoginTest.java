package com.lge.stark.httphandler.user;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lge.stark.Server;
import com.lge.stark.controller.DeviceController;
import com.lge.stark.model.Device;
import com.lge.stark.model.PushType;

import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.anyflow.menton.http.HttpConstants.HeaderValues;
import net.anyflow.menton.http.HttpResponse;
import net.anyflow.menton.http.IHttpClient;
import net.anyflow.menton.http.MockHttpClient;

public class PutLoginTest {

	private static String USER_ID = "37cd2cd5-0a1e-4641-9f5e-6096b16b64d5";
	private static String DEVICE_ID = "09bab373-ff80-42ca-a221-ba3e8345a469";
	private static String DEVICE_ID2 = "f5455162-0004-433c-8e62-d0b75ccbc428";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DeviceController.SELF.create(DEVICE_ID, "testreceiverId", PushType.APNS, false);
		DeviceController.SELF.create(DEVICE_ID2, "testreceiverId", PushType.APNS, false);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DeviceController.SELF.delete(DEVICE_ID);
		DeviceController.SELF.delete(DEVICE_ID2);
	}

	public String loginedUserId(String deviceId) throws Exception {
		JSONObject param = new JSONObject();

		param.put("deviceId", deviceId);

		IHttpClient client = new MockHttpClient(Server.SERVER, Server.BASE_URI + "/user/" + USER_ID + "/login");

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);
		client.httpRequest().setContent(param.toString());

		HttpResponse response = client.put();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));

		if (HttpResponseStatus.OK.equals(response.getStatus()) == false) { throw new Exception("No 200 OK"); }

		List<Device> devices = DeviceController.SELF.get(deviceId, DEVICE_ID2);

		assertThat(devices.size(), is(2));

		if (devices.get(0).isActive()) {
			assertThat(devices.get(1).isActive(), is(false));
		}
		else {
			assertThat(devices.get(1).isActive(), is(true));
		}

		return USER_ID;
	}

	@Test
	public void testService() throws Exception {
		loginedUserId(DEVICE_ID);
	}
}