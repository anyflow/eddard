package com.lge.stark.httphandler.channel;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Test;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;
import com.lge.stark.Server;

import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import net.anyflow.menton.http.HttpConstants.HeaderValues;
import net.anyflow.menton.http.HttpResponse;
import net.anyflow.menton.http.IHttpClient;
import net.anyflow.menton.http.MockHttpClient;

public class PostTest {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PostTest.class);

	private static String INVITER_ID;
	private static List<String> INVITEE_IDS;

	private static String CHANNEL_ID;

	static {
		INVITER_ID = "37cd2cd5-0a1e-4641-9f5e-6096b16b64d5";
		INVITEE_IDS = Lists.newArrayList(
				new String[] { "9658b662-9352-46d5-b778-908bc90204a6", "7db4ee17-5e7c-4a8e-b868-571168f7fcb1" });
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	public static String channelId() throws Exception {
		if (Strings.isNullOrEmpty(CHANNEL_ID)) {
			(new PostTest()).testService();
		}

		return CHANNEL_ID;
	}

	@Test
	public void testService() throws Exception {
		JSONObject param = new JSONObject();

		JSONArray inviteeIdsJson = new JSONArray();
		INVITEE_IDS.forEach(x -> {
			inviteeIdsJson.put(x);
		});

		param.put("name", "sampleChannelName");
		param.put("inviterId", INVITER_ID);
		param.put("inviteeIds", inviteeIdsJson);
		param.put("secretKey", "sampleSecretKey");

		IHttpClient client = new MockHttpClient(Server.SERVER, Server.BASE_URI + "/channel");

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);
		client.httpRequest().setContent(param.toString());

		HttpResponse response = client.post();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));

		String content = response.content().toString(CharsetUtil.UTF_8);

		assertThat(content, containsString("id"));

		CHANNEL_ID = JsonPath.read(content, "$.id");
	}
}