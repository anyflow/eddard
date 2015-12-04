package com.lge.stark.eddard.model;

import com.lge.stark.eddard.Jsonizable;

public class Profile extends Jsonizable {
	private String userId;
	private String deviceId;
	private boolean isLogined;

	public Profile() {
	}

	public Profile(String userId, String deviceId) {
		this.userId = userId;
		this.deviceId = deviceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public boolean isLogined() {
		return isLogined;
	}

	public void setLogined(boolean isLogined) {
		this.isLogined = isLogined;
	}
}