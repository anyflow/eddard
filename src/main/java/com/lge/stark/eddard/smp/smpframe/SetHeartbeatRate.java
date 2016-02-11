package com.lge.stark.eddard.smp.smpframe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SetHeartbeatRate extends Smpframe {

	private int heartbeatRate; // second unit

	@JsonProperty("heartbeatRate")
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