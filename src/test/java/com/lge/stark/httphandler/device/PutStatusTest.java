package com.lge.stark.httphandler.device;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lge.stark.Server;

import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.anyflow.menton.http.HttpConstants.HeaderValues;
import net.anyflow.menton.http.HttpResponse;
import net.anyflow.menton.http.IHttpClient;
import net.anyflow.menton.http.MockHttpClient;

public class PutStatusTest {

	private static String DEVICE_ID = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (DEVICE_ID == null) { return; }

		(new DeleteTest()).delete(DEVICE_ID);
	}

	@Test
	public void testService() throws Exception {
		DEVICE_ID = (new PostTest()).registedDeviceId();

		IHttpClient client = new MockHttpClient(Server.SERVER, Server.BASE_URI + "/device/" + DEVICE_ID + "/status");

		JSONObject param = new JSONObject();

		param.put("isActive", false);

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);
		client.httpRequest().setContent(param.toString());

		HttpResponse response = client.put();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));
	}
}