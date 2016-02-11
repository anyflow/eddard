package com.lge.stark.eddard.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lge.stark.eddard.model.Fault;

public class ErrorStarkService extends Smpframe {

	String code;
	String description;

	public ErrorStarkService() {
		super();
	}

	public ErrorStarkService(String sessionId, Integer smpframeId, Fault fault) {
		super(OpCode.ERROR_INTERNAL_UNKNOWN, sessionId, smpframeId);

		this.code = fault.code();
		this.description = fault.description();
	}

	@JsonProperty("code")
	public String code() {
		return code;
	}

	@JsonProperty("description")
	public String description() {
		return description;
	}

	public void code(String code) {
		this.code = code;
	}

	public void description(String description) {
		this.description = description;
	}

	@Override
	public boolean isResponseRequired() {
		return false;
	}
}