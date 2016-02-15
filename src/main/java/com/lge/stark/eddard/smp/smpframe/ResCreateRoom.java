package com.lge.stark.eddard.smp.smpframe;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResCreateRoom extends Smpframe {

	@JsonProperty("id")
	String id;

	@JsonProperty("messageId")
	String messageId;

	@JsonProperty("createDate")
	Date createDate;

	@JsonProperty("unreadCount")
	int unreadCount;

	public ResCreateRoom() {
		super();
	}

	public ResCreateRoom(String sessionId, Integer smpframeId, String id, String messageId, Date createDate,
			int unreadCount) {
		super(OpCode.RES_CREATE_ROOM, sessionId, smpframeId);

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
