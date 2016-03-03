package com.lge.stark.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LeaveChannel extends Smpframe {

	@JsonProperty("channelId")
	String channelId;

	@JsonProperty("userId")
	String userId;

	public LeaveChannel() {
		super();
	}

	public String channelId() {
		return channelId;
	}

	public String userId() {
		return userId;
	}
}
