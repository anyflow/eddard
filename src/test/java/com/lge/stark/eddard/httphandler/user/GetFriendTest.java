package com.lge.stark.eddard.httphandler.user;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lge.stark.eddard.Jsonizable;
import com.lge.stark.eddard.Server;
import com.lge.stark.eddard.model.User;

import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import net.anyflow.menton.http.HttpConstants.HeaderValues;
import net.anyflow.menton.http.HttpResponse;
import net.anyflow.menton.http.IHttpClient;
import net.anyflow.menton.http.MockHttpClient;

public class GetFriendTest {

	private static String USER_ID = "37cd2cd5-0a1e-4641-9f5e-6096b16b64d5";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	public List<User> getFriends(String userId) throws Exception {
		JSONObject param = new JSONObject();

		IHttpClient client = new MockHttpClient(Server.SERVER, Server.BASE_URI + "/user/" + userId + "/friend");

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);
		client.httpRequest().setContent(param.toString());

		HttpResponse response = client.get();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));

		return Jsonizable.read(response.content().toString(CharsetUtil.UTF_8), new TypeReference<List<User>>() {
		});
	}

	@Test
	public void testService() throws Exception {
		List<User> ret = getFriends(USER_ID);

		assertThat(ret.size(), is(2));
	}
}