package com.lge.stark.eddard.httphandler.channel;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;
import com.lge.stark.eddard.Server;

import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import net.anyflow.menton.http.HttpConstants.HeaderValues;
import net.anyflow.menton.http.HttpResponse;
import net.anyflow.menton.http.IHttpClient;
import net.anyflow.menton.http.MockHttpClient;

public class PostUserTest {

	private static String CHANNEL_ID;

	private static List<String> NEW_INVITEE_IDS;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CHANNEL_ID = PostTest.channelId();

		NEW_INVITEE_IDS = Lists.newArrayList(
				new String[] { "965b4a80-0064-4224-bf71-d95f0b1b1b3e", "fb4177cc-36e0-4341-8f81-63784d49139e" });
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testService() throws Exception {

		JSONObject param = new JSONObject();

		JSONArray inviteeIdsJson = new JSONArray();
		NEW_INVITEE_IDS.stream().forEach(x -> {
			inviteeIdsJson.put(x);
		});

		param.put("users", inviteeIdsJson);

		IHttpClient client = new MockHttpClient(Server.SERVER, Server.BASE_URI + "/channel/" + CHANNEL_ID + "/user");

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);
		client.httpRequest().setContent(param.toString());

		HttpResponse response = client.post();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));

		String content = response.content().toString(CharsetUtil.UTF_8);

		assertThat(content, containsString("id"));

		assertThat(CHANNEL_ID, is(JsonPath.read(content, "$.id").toString()));
	}
}