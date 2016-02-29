package com.lge.stark.model;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.lge.stark.Jsonizable;

public class MessageStatus extends Jsonizable {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MessageStatus.class);

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

	public String getId() {
		return getId(messageId, deviceId);
	}

	public static String getId(String messageId, String deviceId) {
		JSONObject obj = new JSONObject();

		try {
			obj.put("messageId", messageId);
			obj.put("deviceId", deviceId);

			return obj.toString();
		}
		catch (JSONException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}