package com.lge.stark.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteDevice extends Smpframe {

	@JsonProperty("deviceId")
	String deviceId;

	public String deviceId() {
		return deviceId;
	}
}