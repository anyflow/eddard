package com.lge.stark.smp.smpframe;

public class IsAlive extends Smpframe {

	public IsAlive() {
		super();
	}

	public IsAlive(String sessionId, Integer smpframeId) {
		super(OpCode.IS_ALIVE, sessionId, smpframeId);
	}
}