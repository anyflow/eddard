package com.lge.stark.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RetrieveMessages extends Smpframe {

	@JsonProperty("channelId")
	private String channelId;

	public String channelId() {
		return channelId;
	}
}