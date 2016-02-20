package com.lge.stark.eddard.model;

import io.netty.handler.codec.http.HttpResponseStatus;

public enum Fault {

	COMMON_000("COMMON.000", "Unknown error occured.", HttpResponseStatus.INTERNAL_SERVER_ERROR),
	COMMON_001("COMMON.001", "Unknown DB error occured.", HttpResponseStatus.INTERNAL_SERVER_ERROR), 
	COMMON_002("COMMON.002", "Invalid JSON content.", HttpResponseStatus.BAD_REQUEST),
	COMMON_003("COMMON.003", "${replacer}", HttpResponseStatus.BAD_REQUEST),

	DEVICE_001("DEVICE.001", "The Device ID(${replacer}) is already exist.", HttpResponseStatus.BAD_REQUEST), 
	DEVICE_002("DEVICE.002", "Invalid Device ID(${replacer}).", HttpResponseStatus.BAD_REQUEST),

	CHANNEL_001("CHANNEL.001", "The channel(${replacer}) is not found.", HttpResponseStatus.BAD_REQUEST),

	USER_002("USER.002", "Invalid User ID(${replacer}).", HttpResponseStatus.BAD_REQUEST);

	private static final String JSON_TEMPLATE = "{\"error\":{\"code\":\"${code}\",\"description\":\"${description}\"}}";

	private String code;
	private String description;
	private HttpResponseStatus httpResponseStatus;

	Fault(String code, String description, HttpResponseStatus httpResponseStatus) {
		this.code = code;
		this.description = description;
		this.httpResponseStatus = httpResponseStatus;
	}

	public String code() {
		return code;
	}

	public String description() {
		return description;
	}

	public HttpResponseStatus httpResponseStatus() {
		return httpResponseStatus;
	}

	public Fault replaceWith(String replacer) {
		this.description = this.description.replace("${replacer}", replacer);
		return this;
	}

	@Override
	public String toString() {
		return code + " | " + description;
	}

	public static Fault from(String code) {
		for (Fault item : values()) {
			if (item.code().equals(code)) { return item; }
		}

		return null;
	}

	public String toJsonString() {
		return JSON_TEMPLATE.replace("${code}", code()).replace("${description}", description());
	}
}