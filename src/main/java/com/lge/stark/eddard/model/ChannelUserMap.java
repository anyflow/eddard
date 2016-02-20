package com.lge.stark.eddard.model;

import com.lge.stark.eddard.Jsonizable;

public class ChannelUserMap extends Jsonizable {
	private String channelId;
	private String userId;

	public ChannelUserMap() {
	}

	public ChannelUserMap(String channelId, String userId) {
		this.channelId = channelId;
		this.userId = userId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
