package com.lge.stark.eddard.model;

import com.lge.stark.eddard.Jsonizable;

public class Fault extends Jsonizable {
	private String id;
	private String message;

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

	public Fault(String id, String message) {
		this.id = id;
		this.message = message;
	}
}