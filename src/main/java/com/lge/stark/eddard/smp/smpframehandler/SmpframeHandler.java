package com.lge.stark.eddard.smp.smpframehandler;

import com.lge.stark.eddard.smp.session.Session;
import com.lge.stark.eddard.smp.session.SessionNexus;
import com.lge.stark.eddard.smp.smpframe.ErrorInternalUnknown;
import com.lge.stark.eddard.smp.smpframe.ErrorNoSessionAllocated;
import com.lge.stark.eddard.smp.smpframe.OpCode;
import com.lge.stark.eddard.smp.smpframe.Smpframe;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public abstract class SmpframeHandler<T extends Smpframe> extends SimpleChannelInboundHandler<T> {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SmpframeHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {
		Session session = null;

		try {
			session = SessionNexus.SELF.get(msg.sessionId());

			if (msg.opcode().id() != OpCode.CONNECT.id() && session == null) {
				ctx.writeAndFlush(
						new TextWebSocketFrame(new ErrorNoSessionAllocated(msg.responseSmpframeId()).toJsonString()))
						.addListener(ChannelFutureListener.CLOSE);
				return;
			}
			else {
				smpframeReceived(ctx, msg, session);
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);

			if (session != null) {
				session.send(new ErrorInternalUnknown(session.id(), msg.responseSmpframeId()));
			}
			else {
				ctx.channel().writeAndFlush(new TextWebSocketFrame(
						new ErrorInternalUnknown(null, msg.responseSmpframeId()).toJsonString()));
			}
		}
	}

	protected abstract void smpframeReceived(ChannelHandlerContext ctx, T smpframe, Session session) throws Exception;
}