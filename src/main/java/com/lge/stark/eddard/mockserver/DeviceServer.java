package com.lge.stark.eddard.mockserver;

import java.util.Map;

import com.google.common.collect.Maps;
import com.lge.stark.eddard.model.Device;

public class DeviceServer {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceServer.class);

	private static DeviceServer instance;

	private Map<String, Device> db;

	public static DeviceServer instance() {
		if (instance == null) {
			instance = new DeviceServer();
		}

		return instance;
	}

	private DeviceServer() {
		db = Maps.newHashMap();
	}

	public Device get(String deviceId) {
		return db.get(deviceId);
	}

	public void set(Device device) {
		if (db.get(device.getId()) != null) {
			db.remove(device.getId());
		}

		db.put(device.getId(), device);
	}
}