package com.lge.stark.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lge.stark.model.PushType;

public class RegisterDevice extends Smpframe {

	@JsonProperty("deviceId")
	String deviceId;

	@JsonProperty("receiverId")
	String receiverId;

	@JsonProperty("pushType")
	PushType pushType;

	@JsonProperty("isActive")
	boolean isActive;

	public String deviceId() {
		return deviceId;
	}

	public String receiverId() {
		return receiverId;
	}

	public PushType pushType() {
		return pushType;
	}

	public boolean isActive() {
		return isActive;
	}
}