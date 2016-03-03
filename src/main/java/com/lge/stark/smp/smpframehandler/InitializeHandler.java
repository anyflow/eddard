package com.lge.stark.smp.smpframehandler;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lge.stark.FaultException;
import com.lge.stark.gateway.DeviceGateway;
import com.lge.stark.smp.session.Session;
import com.lge.stark.smp.session.SessionNexus;
import com.lge.stark.smp.smpframe.Initialize;
import com.lge.stark.smp.smpframe.OpCode;
import com.lge.stark.smp.smpframe.Smpframe;

import io.netty.channel.ChannelHandlerContext;

public class InitializeHandler extends SmpframeHandler<Initialize> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(InitializeHandler.class);

	@Override
	protected void smpframeReceived(ChannelHandlerContext ctx, Initialize smpframe, Session session)
			throws FaultException {
		session = SessionNexus.SELF.getByDeviceId(smpframe.deviceId());

		if (session != null) {
			SessionNexus.SELF.dispose(session);
		}

		DeviceGateway.SELF.validate(smpframe.deviceId());

		session = new Session(smpframe.deviceId(), ctx);

		SessionNexus.SELF.put(session);

		session.lastIncomingTime(new Date());

		session.send(new Smpframe(OpCode.SET_HEARTBEAT_RATE, session.id(), smpframe.responseSmpframeId()) {
			@JsonProperty("heartbeatRate")
			private int heartbeatRate = 60 * 30; // second unit
		});
	}
}