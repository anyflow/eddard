package com.lge.stark.smp.smpframe;

public class ReturnOk extends Smpframe {

	public ReturnOk(String sessionId, Integer smpframeId) {
		super(OpCode.RETURN_OK, sessionId, smpframeId);
	}
}