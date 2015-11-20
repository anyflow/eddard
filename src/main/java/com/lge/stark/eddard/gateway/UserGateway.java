package com.lge.stark.eddard.gateway;

import org.apache.commons.lang3.NotImplementedException;

import com.lge.stark.eddard.model.Device;

public class UserGateway {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserGateway.class);

	private static UserGateway instance;

	public static UserGateway instance() {
		if (instance == null) {
			instance = new UserGateway();
		}

		return instance;
	}

	public Device getLogined(String userId) {
		throw new NotImplementedException("");
	}
}