package com.lge.stark.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateDeviceStatus extends Smpframe {

	@JsonProperty("deviceId")
	String deviceId;

	@JsonProperty("isActive")
	boolean isActive;

	public String deviceId() {
		return deviceId;
	}

	public boolean isActive() {
		return isActive;
	}
}