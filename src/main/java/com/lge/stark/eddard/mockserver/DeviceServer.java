package com.lge.stark.eddard.mockserver;

import com.lge.stark.eddard.model.Device;

public class DeviceServer extends MockServer<Device> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceServer.class);

	private static DeviceServer instance;

	public static DeviceServer instance() {
		if (instance == null) {
			instance = new DeviceServer();
		}

		return instance;
	}

	public boolean isValid(String deviceId) {
		return true;
	}
}
