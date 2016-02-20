package com.lge.stark.gateway;

import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import com.lge.stark.FaultException;
import com.lge.stark.Settings;
import com.lge.stark.model.Fault;

import io.netty.handler.codec.http.HttpHeaders.Names;
import net.anyflow.menton.http.HttpClient;
import net.anyflow.menton.http.HttpConstants.HeaderValues;
import net.anyflow.menton.http.HttpResponse;

public class GcmGateway {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GcmGateway.class);

	final String appKey;

	public GcmGateway() {
		appKey = Settings.SELF.getProperty("gcm.appKey");
	}

	public void shutdown() {
	}

	public void send(String registrationId, String message) throws FaultException {
		HttpClient client;

		try {
			client = new HttpClient("https://gcm-http.googleapis.com/gcm/send");
		}
		catch (UnsupportedOperationException | URISyntaxException e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		client.httpRequest().headers().add(Names.CONTENT_TYPE, HeaderValues.APPLICATION_JSON);
		client.httpRequest().headers().add(Names.AUTHORIZATION, "key=" + appKey);

		JSONObject body = new JSONObject();
		JSONObject msg = new JSONObject();

		try {
			body.put("to", registrationId);
			msg.put("message", message);
			body.put("data", message);
		}
		catch (JSONException e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_002);
		}

		client.httpRequest().setContent(body.toString());

		HttpResponse response = client.post();

		if (response.getStatus().code() < 300 && response.getStatus().code() >= 200) {
			// TODO success handling
			// https://developers.google.com/cloud-messaging/http-server-ref#params"
		}
		else {
			// TODO failure handling
		}
	}
}