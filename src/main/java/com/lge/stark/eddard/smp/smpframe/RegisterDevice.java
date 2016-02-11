package com.lge.stark.eddard.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lge.stark.eddard.model.PushType;

public class RegisterDevice extends Smpframe {
	String deviceId;
	String receiverId;
	PushType pushType;
	boolean isActive;

	public RegisterDevice() {
		super();

		opcode(OpCode.REGISTER_DEVICE);
	}

	@JsonProperty("deviceId")
	public String deviceId() {
		return deviceId;
	}

	@JsonProperty("receiverId")
	public String receiverId() {
		return receiverId;
	}

	@JsonProperty("pushType")
	public PushType pushType() {
		return pushType;
	}

	@JsonProperty("isActive")
	public boolean isActive() {
		return isActive;
	}

	public void deviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void receiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public void deviceId(PushType pushType) {
		this.pushType = pushType;
	}

	public void deviceId(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public boolean isResponseRequired() {
		return true;
	}
}