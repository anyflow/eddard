package com.lge.stark.smp.smpframe;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResCreateChannel extends Smpframe {

	@JsonProperty("id")
	String id;

	@JsonProperty("messageId")
	String messageId;

	@JsonProperty("createDate")
	Date createDate;

	@JsonProperty("unreadCount")
	int unreadCount;

	public ResCreateChannel() {
		super();
	}

	public ResCreateChannel(String sessionId, Integer smpframeId, String id, String messageId, Date createDate,
			int unreadCount) {
		super(OpCode.RES_CREATE_CHANNEL, sessionId, smpframeId);

		this.id = id;
		this.messageId = messageId;
		this.createDate = createDate;
		this.unreadCount = unreadCount;
	}

	@Override
	public boolean isResponseRequired() {
		return false;
	}
}
