package com.lge.stark.gateway;

import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.net.HttpHeaders;
import com.lge.stark.FaultException;
import com.lge.stark.Settings;
import com.lge.stark.model.Fault;

import io.netty.handler.codec.http.HttpResponseStatus;
import net.anyflow.menton.http.HttpClient;
import net.anyflow.menton.http.HttpConstants.HeaderValues;
import net.anyflow.menton.http.HttpResponse;

public class LgpsGateway {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LgpsGateway.class);

	final String appKey;
	final String baseUri;
	final boolean useInsecureTrustManagerFactory;

	public LgpsGateway() {
		appKey = Settings.SELF.getProperty("lgps.appKey");
		baseUri = Settings.SELF.getProperty("lgps.baseUri");
		useInsecureTrustManagerFactory = Settings.SELF.getBoolean("lgps.useInsecureTrustManagerFactory", true);
	}

	public void send(String receiverId, String message) throws FaultException {
		HttpClient client;
		try {
			client = new HttpClient(baseUri + "/push/v1.0/notification", useInsecureTrustManagerFactory);
		}
		catch (UnsupportedOperationException | URISyntaxException e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		JSONObject obj = new JSONObject();
		JSONObject msg = new JSONObject();

		try {
			JSONArray receivers = new JSONArray();
			receivers.put(receiverId);

			msg.put("message", message);
			obj.put("receiverIds", receivers);
			obj.put("message", msg);
		}
		catch (JSONException e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_002);
		}

		client.httpRequest().headers().add(HttpHeaders.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);
		client.httpRequest().headers().add("x-lge-appKey", appKey);
		client.httpRequest().setContent(obj.toString());

		HttpResponse res = client.post();

		if (res == null || res.getStatus().code() != HttpResponseStatus.OK.code()) {
			// TODO error handling
			return;
		}

		// TODO success handling
	}
}