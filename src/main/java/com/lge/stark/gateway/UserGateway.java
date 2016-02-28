package com.lge.stark.gateway;

import java.util.List;

import com.google.inject.internal.Lists;
import com.lge.stark.FaultException;
import com.lge.stark.controller.DeviceController;
import com.lge.stark.mockserver.ProfileServer;
import com.lge.stark.mockserver.UserServer;
import com.lge.stark.model.Device;
import com.lge.stark.model.Fault;
import com.lge.stark.model.User;

public class UserGateway {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserGateway.class);

	public final static UserGateway SELF;

	static {
		SELF = new UserGateway();
	}

	public void login(String id, String deviceId) throws FaultException {
		if (UserServer.SELF.get(id) == null) { throw new FaultException(Fault.USER_002.replaceWith(id)); }

		List<Device> devices = DeviceController.SELF.get(deviceId);
		if (devices == null
				|| devices.size() <= 0) { throw new FaultException(Fault.DEVICE_002.replaceWith(deviceId)); }

		List<String> deviceIds = ProfileServer.SELF.getDeviceIds(id);

		for (String item : deviceIds) {
			if (DeviceController.SELF.get(item).isEmpty()) {
				continue;
			}

			DeviceController.SELF.updateStatus(item, item.equals(deviceId));
		}
	}

	public void logout(String id) throws FaultException {
		if (UserServer.SELF.get(id) == null) { throw new FaultException(Fault.USER_002.replaceWith(id)); }

		List<String> deviceIds = ProfileServer.SELF.getDeviceIds(id);

		for (String item : deviceIds) {
			DeviceController.SELF.updateStatus(item, false);
		}
	}

	public List<User> get(String... ids) throws FaultException {
		List<User> ret = Lists.newArrayList();

		for (String id : ids) {
			User user = UserServer.SELF.get(id);
			if (user == null) { throw new FaultException(Fault.USER_002.replaceWith(id)); }

			ret.add(user);
		}

		return ret;
	}

	public List<User> getFriends(String id) throws FaultException {
		List<User> users = get(id);
		if (users == null || users.isEmpty()) { throw new FaultException(Fault.USER_002.replaceWith(id)); }

		List<User> ret = Lists.newArrayList();

		for (String friendId : users.get(0).getFriendIds()) {
			User item = UserServer.SELF.get(friendId);
			if (item == null) {
				continue;
			}

			ret.add(item);
		}

		return ret;
	}
}