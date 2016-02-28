package com.lge.stark.gateway;

import com.lge.stark.FaultException;
import com.lge.stark.mockserver.DeviceServer;
import com.lge.stark.model.Fault;

public class DeviceGateway {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceGateway.class);

	public final static DeviceGateway SELF;

	static {
		SELF = new DeviceGateway();
	}

	public void validate(String deviceId) throws FaultException {
		if (DeviceServer.SELF.isValid(deviceId)) { return; }

		throw new FaultException(Fault.DEVICE_002.replaceWith(deviceId));
	}
}