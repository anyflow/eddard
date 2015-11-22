package com.lge.stark.eddard.controller;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import com.lge.stark.eddard.model.Device;

public class DeviceController {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceController.class);

	private static DeviceController instance;

	public static DeviceController instance() {
		if (instance == null) {
			instance = new DeviceController();
		}

		return instance;
	}

	public List<Device> get(List<String> deviceIds) {
		throw new NotImplementedException("");
	}
}
