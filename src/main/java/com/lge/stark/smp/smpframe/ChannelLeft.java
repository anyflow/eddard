package com.lge.stark.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelLeft extends Smpframe {

	@JsonProperty("channelId")
	String channelId;

	@JsonProperty("userId")
	String userId;

	public ChannelLeft(String sessionId, Integer pushframeId, String channelId, String userId) {
		super(OpCode.CHANNEL_LEFT, sessionId, pushframeId);

		this.channelId = channelId;
		this.userId = userId;
	}

	@Override
	public boolean isResponseRequired() {
		return false;
	}
}
