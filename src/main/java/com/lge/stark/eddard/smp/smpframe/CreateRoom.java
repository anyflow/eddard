package com.lge.stark.eddard.smp.smpframe;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRoom extends Smpframe {

	@JsonProperty("name")
	String name;

	@JsonProperty("inviterId")
	String inviterId;

	@JsonProperty("inviteeIds")
	List<String> inviteeIds;

	@JsonProperty("secretKey")
	String secretKey;

	@JsonProperty("message")
	String message;

	public CreateRoom() {
		super();
	}

	@Override
	public boolean isResponseRequired() {
		return false;
	}

	public String name() {
		return name;
	}

	public String inviterId() {
		return inviterId;
	}

	public List<String> inviteeIds() {
		return inviteeIds;
	}

	public String secretKey() {
		return secretKey;
	}

	public String message() {
		return message;
	}
}
