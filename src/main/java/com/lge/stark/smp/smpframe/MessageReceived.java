package com.lge.stark.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageReceived extends Smpframe {

	@JsonProperty("messageId")
	String messageId;

	public String messageId() {
		return messageId;
	}
}
