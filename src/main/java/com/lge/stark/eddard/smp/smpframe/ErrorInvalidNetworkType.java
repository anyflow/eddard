package com.lge.stark.eddard.smp.smpframe;

public class ErrorInvalidNetworkType extends Smpframe {

	public ErrorInvalidNetworkType() {
		super(OpCode.ERROR_INVALID_NETWORKTYPE, null, null);
	}

	public ErrorInvalidNetworkType(String sessionId, Integer pushframeId) {
		super(OpCode.ERROR_INVALID_NETWORKTYPE, sessionId, pushframeId);
	}

	@Override
	public boolean isResponseRequired() {
		return false;
	}
}