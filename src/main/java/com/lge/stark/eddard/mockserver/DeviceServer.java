package com.lge.stark.eddard.mockserver;

import org.elasticsearch.action.get.GetResponse;

import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.gateway.ElasticsearchGateway;

public class DeviceServer {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceServer.class);

	public final static DeviceServer SELF;

	static {
		SELF = new DeviceServer();
	}

	public boolean isValid(String deviceId) throws FaultException {
		GetResponse gr = ElasticsearchGateway.getClient().prepareGet("device", "device", deviceId).get();

		return gr.isExists();
	}
}