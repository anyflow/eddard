package com.lge.stark.smp.smpframe;

public class ErrorInvalidSmpframeFormat extends Smpframe {

	public ErrorInvalidSmpframeFormat() {
		super(OpCode.ERROR_INVALID_SMPFRAME_FORMAT, null, -1);
	}

	@Override
	public boolean isResponseRequired() {
		return false;
	}
}