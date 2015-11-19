package com.lge.stark.eddard;

import io.netty.handler.codec.http.HttpResponseStatus;

public class Fault extends Jsonizable {
	private String id;
	private String message;
	private HttpResponseStatus httpStatus;

	public Fault(String id, String message, HttpResponseStatus httpStatus) {
		this.id = id;
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public Fault(String id, String message) {
		this(id, message, HttpResponseStatus.BAD_REQUEST);
	}

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

	public HttpResponseStatus httpStatus() {
		return httpStatus;
	}

	public void httpStatus(HttpResponseStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
}