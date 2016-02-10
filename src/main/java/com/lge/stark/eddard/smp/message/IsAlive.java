package com.lge.stark.eddard.smp.message;

public class IsAlive extends Smpframe {

	public IsAlive() {
		super();
	}

	public IsAlive(String sessionId, Integer smpframeId) {
		super(OpCode.IS_ALIVE, sessionId, smpframeId);
	}

	@Override
	public boolean isResponseRequired() {
		return true;
	}
}