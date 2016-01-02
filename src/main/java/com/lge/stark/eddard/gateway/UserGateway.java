package com.lge.stark.eddard.gateway;

import java.util.List;

import com.google.inject.internal.Lists;
import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.controller.DeviceController;
import com.lge.stark.eddard.mockserver.ProfileServer;
import com.lge.stark.eddard.mockserver.UserServer;
import com.lge.stark.eddard.model.Device;
import com.lge.stark.eddard.model.Fault;
import com.lge.stark.eddard.model.User;

public class UserGateway {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserGateway.class);

	public final static UserGateway SELF;

	static {
		SELF = new UserGateway();
	}

	public void login(String userId, String deviceId) throws FaultException {
		if (UserServer.SELF.get(userId) == null) { throw new FaultException(Fault.USER_002.replaceWith(userId)); }

		List<Device> devices = DeviceController.SELF.get(Lists.newArrayList(deviceId));
		if (devices == null
				|| devices.size() <= 0) { throw new FaultException(Fault.DEVICE_002.replaceWith(deviceId)); }

		List<String> deviceIds = ProfileServer.SELF.getDeviceIds(userId);

		for (String item : deviceIds) {
			DeviceController.SELF.updateStatus(item, item.equals(deviceId));
		}
	}

	public void logout(String userId) throws FaultException {
		if (UserServer.SELF.get(userId) == null) { throw new FaultException(Fault.USER_002.replaceWith(userId)); }

		List<String> deviceIds = ProfileServer.SELF.getDeviceIds(userId);

		for (String item : deviceIds) {
			DeviceController.SELF.updateStatus(item, false);
		}
	}

	public List<User> getFriends(String userId) throws FaultException {
		User user = UserServer.SELF.get(userId);
		if (user == null) { throw new FaultException(Fault.USER_002.replaceWith(userId)); }

		List<User> ret = Lists.newArrayList();

		for (String id : user.getFriends()) {
			User item = UserServer.SELF.get(id);
			if (item == null) {
				continue;
			}

			ret.add(item);
		}

		return ret;
	}
}