package com.lge.stark.gateway;

import com.lge.stark.FaultException;
import com.lge.stark.mockserver.DeviceServer;

public class DeviceGateway {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceGateway.class);

	public final static DeviceGateway SELF;

	static {
		SELF = new DeviceGateway();
	}

	public boolean isValid(String deviceId) throws FaultException {
		return DeviceServer.SELF.isValid(deviceId);
	}
}