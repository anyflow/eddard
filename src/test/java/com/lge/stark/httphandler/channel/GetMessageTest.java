package com.lge.stark.httphandler.channel;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

public class GetMessageTest {

	private static String CHANNEL_ID;

	@SuppressWarnings("static-access")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CHANNEL_ID = PostTest.channelId();
		Thread.currentThread().sleep(500); // for indexing latency...
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testService() throws Exception {
		IHttpClient client = new MockHttpClient(Server.SERVER,
				Server.BASE_URI + "/channel/" + CHANNEL_ID + "/messages");

		client.httpRequest().headers().set(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);

		HttpResponse response = client.get();

		assertThat(response.getStatus(), is(HttpResponseStatus.OK));
	}
}
