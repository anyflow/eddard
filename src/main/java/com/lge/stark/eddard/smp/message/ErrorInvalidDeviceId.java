package com.lge.stark.eddard.smp.message;

public class ErrorInvalidDeviceId extends Smpframe {

	public ErrorInvalidDeviceId() {
		super();
	}

	public ErrorInvalidDeviceId(Integer smpframeId) {
		super(OpCode.ERROR_INVALID_DEVICEID, null, smpframeId);
	}

	@Override
	public boolean isResponseRequired() {
		return false;
	}
}