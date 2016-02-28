package com.lge.stark.smp.smpframehandler;

import com.lge.stark.FaultException;
import com.lge.stark.smp.session.Session;
import com.lge.stark.smp.session.SessionNexus;
import com.lge.stark.smp.smpframe.ErrorStarkService;
import com.lge.stark.smp.smpframe.OpCode;
import com.lge.stark.smp.smpframe.Smpframe;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public abstract class SmpframeHandler<T extends Smpframe> extends SimpleChannelInboundHandler<T> {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SmpframeHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {
		logger.debug("handing {} Smpframe...", msg.getClass().getSimpleName());

		Session session = null;

		try {
			session = SessionNexus.SELF.get(msg.sessionId());

			if (msg.opcode().id() != OpCode.INITIALIZE.id() && session == null) {

				ctx.writeAndFlush(new TextWebSocketFrame(
						Smpframe.createDefault(OpCode.ERROR_NO_SESSION_ALLOCATED, null, msg.responseSmpframeId())
								.toJsonString()))
						.addListener(new ChannelFutureListener() {
							@Override
							public void operationComplete(ChannelFuture future) throws Exception {
								logger.debug("None session sending finished. sessionID|smpframe - {}|{}", null,
										msg.toString());
							}
						}).addListener(ChannelFutureListener.CLOSE);
				return;
			}
			else {
				smpframeReceived(ctx, msg, session);
			}
		}
		catch (FaultException e) {
			logger.error(e.getMessage(), e);

			if (session != null) {
				session.send(new ErrorStarkService(session.id(), msg.responseSmpframeId(), e.fault()));
			}
			else {
				ctx.channel().writeAndFlush(new ErrorStarkService(null, msg.responseSmpframeId(), e.fault()))
						.addListener(new ChannelFutureListener() {
							@Override
							public void operationComplete(ChannelFuture future) throws Exception {
								logger.debug("None session sending finished. sessionID|smpframe - {}|{}", null,
										msg.toString());
							}
						}).addListener(ChannelFutureListener.CLOSE);
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);

			if (session != null) {
				session.send(
						Smpframe.createDefault(OpCode.ERROR_INTERNAL_UNKNOWN, session.id(), msg.responseSmpframeId()));
			}
			else {
				ctx.channel()
						.writeAndFlush(new TextWebSocketFrame(
								Smpframe.createDefault(OpCode.ERROR_INTERNAL_UNKNOWN, null, msg.responseSmpframeId())
										.toJsonString()))
						.addListener(new ChannelFutureListener() {
							@Override
							public void operationComplete(ChannelFuture future) throws Exception {
								logger.debug("None session sending finished. sessionID|smpframe - {}|{}", null,
										msg.toString());
							}
						});
			}
		}
	}

	protected abstract void smpframeReceived(ChannelHandlerContext ctx, T smpframe, Session session)
			throws FaultException;
}