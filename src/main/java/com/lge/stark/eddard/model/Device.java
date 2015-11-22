package com.lge.stark.eddard.model;

import java.util.Date;

import com.lge.stark.eddard.Jsonizable;

public class Device extends Jsonizable {

	public enum PushType {
		LGPS, GCM, APNS
	}

	private String id;
	private String userId;
	private PushType pushType;
	private String receiverId;
	private boolean isActive;
	private Date createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public PushType getPushType() {
		return pushType;
	}

	public void setPushType(PushType pushType) {
		this.pushType = pushType;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void isActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}