package com.lge.stark.eddard.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateDeviceStatus extends Smpframe {
	String deviceId;
	boolean isActive;

	public UpdateDeviceStatus() {
		super();

		opcode(OpCode.UPDATE_DEVICE_STATUS);
	}

	@JsonProperty("deviceId")
	public String deviceId() {
		return deviceId;
	}

	@JsonProperty("isActive")
	public boolean isActive() {
		return isActive;
	}

	public void deviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void deviceId(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public boolean isResponseRequired() {
		return true;
	}
}