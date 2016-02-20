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

	public RegisterDevice() {
		super();

		opcode(OpCode.REGISTER_DEVICE);
	}

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

	public void deviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void receiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public void pushType(PushType pushType) {
		this.pushType = pushType;
	}

	public void isActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public boolean isResponseRequired() {
		return true;
	}
}