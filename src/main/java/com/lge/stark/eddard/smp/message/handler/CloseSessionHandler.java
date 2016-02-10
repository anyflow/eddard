package com.lge.stark.eddard.smp.message.handler;

import com.lge.stark.eddard.smp.message.ErrorNoSessionAllocated;
import com.lge.stark.eddard.smp.message.IsAlive;
import com.lge.stark.eddard.smp.message.ReturnOk;
import com.lge.stark.eddard.smp.session.Session;
import com.lge.stark.eddard.smp.session.SessionNexus;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class CloseSessionHandler extends SimpleChannelInboundHandler<IsAlive> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CloseSessionHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IsAlive msg) throws Exception {
		Session session = SessionNexus.SELF.get(msg.sessionId());

		if (session == null) {
			ctx.writeAndFlush(
					new TextWebSocketFrame(new ErrorNoSessionAllocated(msg.responseSmpframeId()).toJsonString()))
					.addListener(ChannelFutureListener.CLOSE);
		}

		session.send(new ReturnOk(session.id(), msg.responseSmpframeId()));

		SessionNexus.SELF.dispose(session);
	}
}