package com.lge.stark.smp.smpframe;

public class ErrorInternalUnknown extends Smpframe {

	public ErrorInternalUnknown() {
		super();
	}

	public ErrorInternalUnknown(String sessionId, Integer smpframeId) {
		super(OpCode.ERROR_INTERNAL_UNKNOWN, sessionId, smpframeId);
	}

	@Override
	public boolean isResponseRequired() {
		return false;
	}
}