package com.lge.stark.eddard.smp.smpframehandler;

import com.lge.stark.eddard.smp.session.Session;
import com.lge.stark.eddard.smp.session.SessionNexus;
import com.lge.stark.eddard.smp.smpframe.IsAlive;
import com.lge.stark.eddard.smp.smpframe.ReturnOk;

import io.netty.channel.ChannelHandlerContext;

public class CloseSessionHandler extends SmpframeHandler<IsAlive> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CloseSessionHandler.class);

	@Override
	protected void smpframeReceived(ChannelHandlerContext ctx, IsAlive smpframe, Session session) throws Exception {
		session.send(new ReturnOk(session.id(), smpframe.responseSmpframeId()));

		SessionNexus.SELF.dispose(session);
	}
}