package com.lge.stark.eddard.gateway;

import java.util.List;

import com.google.inject.internal.Lists;
import com.lge.stark.eddard.Fault;
import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.controller.DeviceController;
import com.lge.stark.eddard.mockserver.ProfileServer;
import com.lge.stark.eddard.mockserver.UserServer;
import com.lge.stark.eddard.model.Device;

public class UserGateway {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserGateway.class);

	public final static UserGateway SELF;

	static {
		SELF = new UserGateway();
	}

	public void login(String userId, String deviceId) throws FaultException {
		if (UserServer.SELF
				.isValid(userId) == false) { throw new FaultException(new Fault("1", "invalid user ID : " + userId)); }

		List<Device> devices = DeviceController.SELF.get(Lists.newArrayList(deviceId));
		if (devices == null
				|| devices.size() <= 0) { throw new FaultException(new Fault("2", "invalid device ID : " + deviceId)); }

		ProfileServer.SELF.setLogin(userId, deviceId);
	}

	public void logout(String userId) throws FaultException {
		if (UserServer.SELF
				.isValid(userId) == false) { throw new FaultException(new Fault("1", "invalid user ID : " + userId)); }

		ProfileServer.SELF.setLogout(userId);
	}
}