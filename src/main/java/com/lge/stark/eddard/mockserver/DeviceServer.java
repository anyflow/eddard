package com.lge.stark.eddard.mockserver;

import org.elasticsearch.action.get.GetResponse;

import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.gateway.ElasticsearchGateway;
import com.lge.stark.eddard.model.Fault;

public class DeviceServer {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceServer.class);

	public final static DeviceServer SELF;

	static {
		SELF = new DeviceServer();
	}

	public boolean isValid(String deviceId) throws FaultException {
		try {
			GetResponse gr = ElasticsearchGateway.getClient().prepareGet("device", "device", deviceId).get();

			return gr.isExists();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}
	}
}