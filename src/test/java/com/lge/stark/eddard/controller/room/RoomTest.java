package com.lge.stark.eddard.controller.room;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.jayway.jsonpath.JsonPath;
import com.lge.stark.eddard.ApiTestCase;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import net.anyflow.menton.http.HttpClient;
import net.anyflow.menton.http.HttpResponse;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoomTest extends ApiTestCase {

	@BeforeClass
	public static void setup() {

	}

	@AfterClass
	public static void teardown() {

	}

	final String address = "http://localhost:8080/room";

	@SuppressWarnings("unused")
	private String roomId;

	@Test
	public void test1_POST() throws UnsupportedOperationException, URISyntaxException, JSONException {

		JSONObject param = new JSONObject();

		param.put("name", "sampleRoomName");
		param.put("inviterId", "aaa");
		param.put("inviteeIds", new JSONArray());
		param.put("secretKey", "sampleSecretKey");

		HttpClient client = new HttpClient(address);
		client.httpRequest().setContent(param.toString());

		HttpResponse response = client.post();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));

		String content = response.content().toString(CharsetUtil.UTF_8);

		assertThat(content, containsString("roomId"));

		roomId = JsonPath.read(content, "$.roomId");
	}
}