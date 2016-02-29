package com.lge.stark.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageReceived extends Smpframe {

	@JsonProperty("messageId")
	String messageId;

	public MessageReceived() {
		super();
	}

	public String messageId() {
		return messageId;
	}

	@Override
	public boolean isResponseRequired() {
		return false;
	}
}
