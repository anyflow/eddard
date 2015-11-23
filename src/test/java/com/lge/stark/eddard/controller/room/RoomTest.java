package com.lge.stark.eddard.controller.room;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;
import com.lge.stark.eddard.ApiTestCase;
import com.lge.stark.eddard.ApiTestSuite;

import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import net.anyflow.menton.http.HttpConstants.HeaderValues;
import net.anyflow.menton.http.HttpResponse;
import net.anyflow.menton.http.IHttpClient;
import net.anyflow.menton.http.MockHttpClient;
import net.anyflow.menton.http.MockHttpServer;
import net.minidev.json.JSONArray;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoomTest extends ApiTestCase {

	private static MockHttpServer SERVER;
	private static String INVITER_ID;
	private static List<String> INVITEE_IDS;

	private static String BASE_ADDRESS;

	private static String ROOM_ID;

	@BeforeClass
	public static void setup() {
		SERVER = ApiTestSuite.server();

		BASE_ADDRESS = "http://localhost:8080/room";

		INVITER_ID = "37cd2cd5-0a1e-4641-9f5e-6096b16b64d5";
		INVITEE_IDS = Lists.newArrayList(
				new String[] { "965b4a80-0064-4224-bf71-d95f0b1b1b3e", "fb4177cc-36e0-4341-8f81-63784d49139e" });
	}

	@AfterClass
	public static void teardown() {

	}

	@Test
	public void test1_Post() throws UnsupportedOperationException, URISyntaxException, JSONException {

		JSONObject param = new JSONObject();

		JSONArray inviteeIdsJson = new JSONArray();
		INVITEE_IDS.forEach(x -> {
			inviteeIdsJson.add(x);
		});

		param.put("name", "sampleRoomName");
		param.put("inviterId", INVITER_ID);
		param.put("inviteeIds", inviteeIdsJson);
		param.put("secretKey", "sampleSecretKey");
		param.put("message", "test message");

		IHttpClient client = new MockHttpClient(SERVER, BASE_ADDRESS);

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);
		client.httpRequest().setContent(param.toString());

		HttpResponse response = client.post();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));

		String content = response.content().toString(CharsetUtil.UTF_8);

		assertThat(content, containsString("roomId"));

		ROOM_ID = JsonPath.read(content, "$.roomId");
	}

	@Test
	public void test2_Get() throws UnsupportedOperationException, URISyntaxException, JSONException {

		IHttpClient client = new MockHttpClient(SERVER, BASE_ADDRESS + "/" + ROOM_ID);

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);

		HttpResponse response = client.get();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));

		String content = response.content().toString(CharsetUtil.UTF_8);

		assertThat(content, containsString("createDate"));
	}
}