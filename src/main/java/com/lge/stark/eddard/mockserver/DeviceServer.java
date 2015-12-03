package com.lge.stark.eddard.mockserver;

import com.lge.stark.eddard.model.Device;

public class DeviceServer extends MockServer<Device> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceServer.class);

	public final static DeviceServer SELF;

	static {
		SELF = new DeviceServer();
	}

	public boolean isValid(String deviceId) {
		return true;
	}
}
