package com.lge.stark.smp.smpframe;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetUsers extends Smpframe {

	@JsonProperty("userIds")
	List<String> userIds;

	public List<String> userIds() {
		return userIds;
	}
}