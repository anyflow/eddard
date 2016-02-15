package com.lge.stark.eddard.smp.smpframe;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lge.stark.eddard.model.User;

public class ResGetFriends extends Smpframe {

	@JsonProperty("users")
	List<User> users;

	public ResGetFriends() {
		super();
	}

	public ResGetFriends(String sessionId, Integer smpframeId, List<User> users) {
		super(OpCode.RES_GET_FRIENDS, sessionId, smpframeId);

		this.users = users;
	}

	@Override
	public boolean isResponseRequired() {
		return false;
	}
}