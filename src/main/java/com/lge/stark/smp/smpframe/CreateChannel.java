package com.lge.stark.smp.smpframe;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateChannel extends Smpframe {

	@JsonProperty("name")
	String name;

	@JsonProperty("inviterId")
	String inviterId;

	@JsonProperty("inviteeIds")
	List<String> inviteeIds;

	@JsonProperty("secretKey")
	String secretKey;

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
}
