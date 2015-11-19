package com.lge.stark.eddard.model;

import com.lge.stark.eddard.Jsonizable;

public class RoomUserMap extends Jsonizable {
	private String roomId;
	private String userId;

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
