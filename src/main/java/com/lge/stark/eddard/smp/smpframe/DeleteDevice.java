package com.lge.stark.eddard.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteDevice extends Smpframe {
	String deviceId;

	public DeleteDevice() {
		super();

		opcode(OpCode.DELETE_DEVICE);
	}

	@JsonProperty("deviceId")
	public String deviceId() {
		return deviceId;
	}

	public void deviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public boolean isResponseRequired() {
		return true;
	}
}