package com.lge.stark.httphandler.device;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lge.stark.IdGenerator;
import com.lge.stark.Server;

import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.anyflow.menton.http.HttpConstants.HeaderValues;
import net.anyflow.menton.http.HttpResponse;
import net.anyflow.menton.http.IHttpClient;
import net.anyflow.menton.http.MockHttpClient;

public class PostTest {

	private static String DEVICE_ID = "09bab373-ff80-42ca-a221-ba3e8345a469";
	private static String RECEIVER_ID = IdGenerator.newId();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		(new DeleteTest()).delete(DEVICE_ID);
	}

	public void regist(String deviceId) throws Exception {
		JSONObject param = new JSONObject();

		param.put("deviceId", deviceId);
		param.put("receiverId", RECEIVER_ID);
		param.put("isActive", true);
		param.put("type", "LGPS");

		IHttpClient client = new MockHttpClient(Server.SERVER, Server.BASE_URI + "/device");

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);
		client.httpRequest().setContent(param.toString());

		HttpResponse response = client.post();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));

		if (HttpResponseStatus.OK.equals(response.getStatus()) == false) { throw new Exception("No 200 OK"); }
	}

	public String registedDeviceId() throws Exception {
		regist(DEVICE_ID);

		return DEVICE_ID;
	}

	@Test
	public void testService() throws Exception {
		regist(DEVICE_ID);
	}
}