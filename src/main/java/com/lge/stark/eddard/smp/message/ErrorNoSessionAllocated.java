package com.lge.stark.eddard.smp.message;

public class ErrorNoSessionAllocated extends Smpframe {

	public ErrorNoSessionAllocated() {
		super(OpCode.ERROR_NO_SESSION_ALLOCATED, null, null);
	}

	public ErrorNoSessionAllocated(Integer smpframeId) {
		super(OpCode.ERROR_NO_SESSION_ALLOCATED, null, smpframeId);
	}

	@Override
	public boolean isResponseRequired() {
		return false;
	}
}