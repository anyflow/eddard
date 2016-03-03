package com.lge.stark.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Initialize extends Smpframe {

	@JsonProperty("deviceId")
	private String deviceId;

	@JsonProperty("networkType")
	private String networkType;

	public String deviceId() {
		return deviceId;
	}

	public String networkType() {
		return networkType;
	}
}