package com.lge.stark.eddard.gateway;

public class UserGateway {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserGateway.class);

	public final static UserGateway SELF;

	static {
		SELF = new UserGateway();
	}
}