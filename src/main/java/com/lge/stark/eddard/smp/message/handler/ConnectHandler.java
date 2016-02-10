package com.lge.stark.eddard.smp.message.handler;

import java.util.Date;

import com.lge.stark.eddard.smp.message.Connect;
import com.lge.stark.eddard.smp.message.SetHeartbeatRate;
import com.lge.stark.eddard.smp.session.Session;
import com.lge.stark.eddard.smp.session.SessionNexus;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ConnectHandler extends SimpleChannelInboundHandler<Connect> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ConnectHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Connect msg) throws Exception {
		Session session = SessionNexus.SELF.getByDeviceId(msg.deviceId());

		if (session != null) {
			SessionNexus.SELF.dispose(session);
		}

		session = new Session(msg.deviceId(), ctx);

		SessionNexus.SELF.put(session);

		session.lastIncomingTime(new Date());

		session.send(new SetHeartbeatRate(session.id(), msg.responseSmpframeId(), 60 * 30));
	}
}