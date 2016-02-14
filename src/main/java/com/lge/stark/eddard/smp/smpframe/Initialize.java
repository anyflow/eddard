package com.lge.stark.eddard.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Initialize extends Smpframe {

	@JsonProperty("deviceId")
	private String deviceId;

	@JsonProperty("networkType")
	private String networkType;

	public Initialize() {
		super(OpCode.INITIALIZE, null, null);
	}

	public Initialize(int smpframeId, String deviceId, String networkType) {
		super(OpCode.INITIALIZE, null, smpframeId);

		this.deviceId = deviceId;
		this.networkType = networkType;
	}

	public String deviceId() {
		return deviceId;
	}

	public void deviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String networkType() {
		return networkType;
	}

	public void networkType(String networkType) {
		this.networkType = networkType;
	}

	@Override
	public boolean isResponseRequired() {
		return true;
	}
}