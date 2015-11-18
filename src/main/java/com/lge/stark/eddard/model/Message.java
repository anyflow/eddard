package com.lge.stark.eddard.model;

import java.util.Date;

public class Message extends SessionMembers {
	private String id;
	private String message;
	private Byte readCount;
	private Date createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Byte getReadCount() {
		return readCount;
	}

	public void setReadCount(Byte readCount) {
		this.readCount = readCount;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}