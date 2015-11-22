package com.lge.stark.eddard.gateway;

import com.lge.stark.eddard.mockserver.DeviceServer;

public class DeviceGateway {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceGateway.class);

	private static DeviceGateway instance;

	public static DeviceGateway instance() {
		if (instance == null) {
			instance = new DeviceGateway();
		}

		return instance;
	}

	public boolean isValid(String deviceId) {
		return DeviceServer.instance().isValid(deviceId);
	}
}