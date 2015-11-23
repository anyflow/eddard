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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeviceTest extends ApiTestCase {

	@BeforeClass
	public static void setup() {

	}

	@AfterClass
	public static void teardown() {

	}

	final String address = "http://localhost:8080/device";

	private static String deviceId = "09bab373-ff80-42ca-a221-ba3e8345a469";

	@Test
	public void test1_Post() throws UnsupportedOperationException, URISyntaxException, JSONException {

		JSONObject param = new JSONObject();

		param.put("deviceId", deviceId);
		param.put("receiverId", IdGenerator.newId());
		param.put("isActive", false);
		param.put("type", "LGPS");

		IHttpClient client = new MockHttpClient(ApiTestSuite.server(), address);

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);
		client.httpRequest().setContent(param.toString());

		HttpResponse response = client.post();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));
	}

	@Test
	public void test2_PutStatus() throws UnsupportedOperationException, URISyntaxException, JSONException {

		IHttpClient client = new MockHttpClient(ApiTestSuite.server(), address + "/" + deviceId + "/status");

		JSONObject param = new JSONObject();

		param.put("isActive", true);

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);
		client.httpRequest().setContent(param.toString());

		HttpResponse response = client.put();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));
	}

	@Test
	public void test3_Delete() throws UnsupportedOperationException, URISyntaxException, JSONException {

		IHttpClient client = new MockHttpClient(ApiTestSuite.server(), address + "/" + deviceId);

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);

		HttpResponse response = client.delete();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));
	}
}