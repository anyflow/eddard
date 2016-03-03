package com.lge.stark.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lge.stark.model.Fault;

public class ErrorStarkService extends Smpframe {

	@JsonProperty("code")
	String code;

	@JsonProperty("description")
	String description;

	public ErrorStarkService(String sessionId, Integer smpframeId, Fault fault) {
		super(OpCode.ERROR_STARK_SERVICE, sessionId, smpframeId);

		this.code = fault.code();
		this.description = fault.description();
	}
}