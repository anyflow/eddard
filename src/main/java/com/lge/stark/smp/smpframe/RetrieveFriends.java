package com.lge.stark.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RetrieveFriends extends Smpframe {

	@JsonProperty("userId")
	private String userId;

	public String userId() {
		return userId;
	}
}