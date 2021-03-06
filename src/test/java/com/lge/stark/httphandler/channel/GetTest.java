package com.lge.stark.httphandler.channel;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lge.stark.Server;

import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import net.anyflow.menton.http.HttpConstants.HeaderValues;
import net.anyflow.menton.http.HttpResponse;
import net.anyflow.menton.http.IHttpClient;
import net.anyflow.menton.http.MockHttpClient;

public class GetTest {

	private static String CHANNEL_ID;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CHANNEL_ID = PostTest.channelId();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testService() throws Exception {
		IHttpClient client = new MockHttpClient(Server.SERVER, Server.BASE_URI + "/channel/" + CHANNEL_ID);

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);

		HttpResponse response = client.get();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));

		String content = response.content().toString(CharsetUtil.UTF_8);

		assertThat(content, containsString("createDate"));
	}
}
