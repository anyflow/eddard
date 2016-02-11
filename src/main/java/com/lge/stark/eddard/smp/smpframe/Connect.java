package com.lge.stark.eddard.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Connect extends Smpframe {

	private String deviceId;
	private String networkType;

	public Connect() {
		super(OpCode.CONNECT, null, null);
	}

	public Connect(int smpframeId, String deviceId, String networkType) {
		super(OpCode.CONNECT, null, smpframeId);

		this.deviceId = deviceId;
		this.networkType = networkType;
	}

	@JsonProperty("deviceId")
	public String deviceId() {
		return deviceId;
	}

	public void deviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@JsonProperty("networkType")
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