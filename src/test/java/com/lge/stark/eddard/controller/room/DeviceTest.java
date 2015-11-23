package com.lge.stark.eddard.controller.room;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.lge.stark.eddard.ApiTestCase;
import com.lge.stark.eddard.ApiTestSuite;
import com.lge.stark.eddard.IdGenerator;

import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.anyflow.menton.http.HttpConstants.HeaderValues;
import net.anyflow.menton.http.HttpResponse;
import net.anyflow.menton.http.IHttpClient;
import net.anyflow.menton.http.MockHttpClient;
import net.anyflow.menton.http.MockHttpServer;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeviceTest extends ApiTestCase {

	private static MockHttpServer SERVER;
	private static String DEVICE_ID;
	private static String BASE_ADDRESS;

	@BeforeClass
	public static void setup() {
		SERVER = ApiTestSuite.server();
		DEVICE_ID = "09bab373-ff80-42ca-a221-ba3e8345a469";
		BASE_ADDRESS = "http://localhost:8080/device";
	}

	@AfterClass
	public static void teardown() {

	}

	@Test
	public void test1_Post() throws UnsupportedOperationException, URISyntaxException, JSONException {

		JSONObject param = new JSONObject();

		param.put("deviceId", DEVICE_ID);
		param.put("receiverId", IdGenerator.newId());
		param.put("isActive", false);
		param.put("type", "LGPS");

		IHttpClient client = new MockHttpClient(SERVER, BASE_ADDRESS);

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);
		client.httpRequest().setContent(param.toString());

		HttpResponse response = client.post();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));
	}

	@Test
	public void test2_PutStatus() throws UnsupportedOperationException, URISyntaxException, JSONException {

		IHttpClient client = new MockHttpClient(SERVER, BASE_ADDRESS + "/" + DEVICE_ID + "/status");

		JSONObject param = new JSONObject();

		param.put("isActive", true);

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);
		client.httpRequest().setContent(param.toString());

		HttpResponse response = client.put();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));
	}

	@Test
	public void test3_Delete() throws UnsupportedOperationException, URISyntaxException, JSONException {

		IHttpClient client = new MockHttpClient(SERVER, BASE_ADDRESS + "/" + DEVICE_ID);

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);

		HttpResponse response = client.delete();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));
	}
}