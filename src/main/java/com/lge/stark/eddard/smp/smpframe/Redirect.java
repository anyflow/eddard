package com.lge.stark.eddard.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Redirect extends Smpframe {

	private String address;

	@JsonProperty("address")
	public String address() {
		return address;
	}

	public Redirect(String sessionId, int smpframeId, String address) {
		super(OpCode.REDIRECT, sessionId, smpframeId);

		this.address = address;
	}

	public Redirect() {
		super(OpCode.REDIRECT, null, null);
	}

	@Override
	public boolean isResponseRequired() {
		return false;
	}
}