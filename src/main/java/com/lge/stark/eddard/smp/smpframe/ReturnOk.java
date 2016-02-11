package com.lge.stark.eddard.smp.smpframe;

public class ReturnOk extends Smpframe {

	public ReturnOk() {
		super();
	}

	public ReturnOk(String sessionId, Integer smpframeId) {
		super(OpCode.RETURN_OK, sessionId, smpframeId);
	}

	@Override
	public boolean isResponseRequired() {
		return false;
	}
}