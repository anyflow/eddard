package com.lge.stark.eddard.model;

import com.lge.stark.eddard.Jsonizable;

public class SessionMembers extends Jsonizable {
	private String sessionId;
	private String userId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
