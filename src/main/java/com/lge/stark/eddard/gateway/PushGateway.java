package com.lge.stark.eddard.gateway;

import org.apache.commons.lang3.NotImplementedException;

import com.lge.stark.eddard.model.Device;

public class PushGateway {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PushGateway.class);

	private static PushGateway instance;

	public static PushGateway instance() {
		if (instance == null) {
			instance = new PushGateway();
		}

		return instance;
	}

	public void sendMessage(Device.PushType pushType, String receiverId, String message) {
		throw new NotImplementedException("");
	}
}