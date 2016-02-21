package com.lge.stark.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateMessage extends Smpframe {

	@JsonProperty("channelId")
	String channelId;

	@JsonProperty("creatorId")
	String creatorId;

	@JsonProperty("text")
	String text;

	public CreateMessage() {
		super();
	}

	public String channelId() {
		return channelId;
	}

	public String creatorId() {
		return creatorId;
	}

	public String text() {
		return text;
	}

	@Override
	public boolean isResponseRequired() {
		return true;
	}
}
