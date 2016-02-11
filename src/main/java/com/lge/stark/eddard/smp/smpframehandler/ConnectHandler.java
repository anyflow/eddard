package com.lge.stark.eddard.smp.smpframehandler;

import java.util.Date;

import com.lge.stark.eddard.smp.session.Session;
import com.lge.stark.eddard.smp.session.SessionNexus;
import com.lge.stark.eddard.smp.smpframe.Connect;
import com.lge.stark.eddard.smp.smpframe.SetHeartbeatRate;

import io.netty.channel.ChannelHandlerContext;

public class ConnectHandler extends SmpframeHandler<Connect> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ConnectHandler.class);

	@Override
	protected void smpframeReceived(ChannelHandlerContext ctx, Connect smpframe, Session session) throws Exception {
		session = SessionNexus.SELF.getByDeviceId(smpframe.deviceId());

		if (session != null) {
			SessionNexus.SELF.dispose(session);
		}

		session = new Session(smpframe.deviceId(), ctx);

		SessionNexus.SELF.put(session);

		session.lastIncomingTime(new Date());

		session.send(new SetHeartbeatRate(session.id(), smpframe.responseSmpframeId(), 60 * 30));
	}
}