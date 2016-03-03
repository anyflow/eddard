package com.lge.stark.smp.smpframe;

public class CloseSession extends Smpframe {

	public CloseSession() {
		super();
	}

	public CloseSession(String sessionId, Integer smpframeId) {
		super(OpCode.CLOSE_SESSION, sessionId, smpframeId);
	}
}