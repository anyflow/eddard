package com.lge.stark.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SetHeartbeatRate extends Smpframe {

	@JsonProperty("heartbeatRate")
	private int heartbeatRate; // second unit

	public int heartbeatRate() {
		return heartbeatRate;
	}

	public SetHeartbeatRate(String sessionId, int pushframeId, int heartbeatRate) {
		super(OpCode.SET_HEARTBEAT_RATE, sessionId, pushframeId);

		this.heartbeatRate = heartbeatRate;
	}

	public SetHeartbeatRate() {
		super(OpCode.SET_HEARTBEAT_RATE, null, null);
	}

	@Override
	public boolean isResponseRequired() {
		return true;
	}
}