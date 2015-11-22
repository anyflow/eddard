package com.lge.stark.eddard.model;

import java.util.Date;

import com.lge.stark.eddard.Jsonizable;

public class MessageStatus extends Jsonizable {

	public enum Status {

		PEND("pend"), SENT("sent"), READ("read");

		private final String description;

		Status(String description) {
			this.description = description;
		}

		public String description() {
			return description;
		}

		@Override
		public String toString() {
			return description;
		}

		public static Status from(String description) {
			if (description == null) { return null; }

			for (Status item : values()) {
				if (item.description().equalsIgnoreCase(description.trim())) { return item; }
			}

			return null;
		}
	}

	private String messageId;
	private String deviceId;
	private Status status;
	private Date createDate;
	private Date sentDate;
	private Date readDate;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public Date getReadDate() {
		return readDate;
	}

	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}
}