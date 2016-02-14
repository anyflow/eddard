package com.lge.stark.eddard.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetFriends extends Smpframe {

	@JsonProperty("userId")
	private String userId;

	public GetFriends() {
		super();
	}

	public String userId() {
		return userId;
	}

	public void userId(String userId) {
		this.userId = userId;
	}

	@Override
	public boolean isResponseRequired() {
		return true;
	}
}