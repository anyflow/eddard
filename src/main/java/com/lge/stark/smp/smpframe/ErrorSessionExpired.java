package com.lge.stark.smp.smpframe;

public class ErrorSessionExpired extends Smpframe {

	public ErrorSessionExpired() {
		super(OpCode.ERROR_SESSION_EXPIRED, null, null);
	}

	public ErrorSessionExpired(String sessionId, Integer smpframeId) {
		super(OpCode.ERROR_SESSION_EXPIRED, sessionId, smpframeId);
	}

	@Override
	public boolean isResponseRequired() {
		return false;
	}
}