package com.lge.stark.eddard.smp.smpframehandler;

import java.util.List;

import com.lge.stark.eddard.Settings;
import com.lge.stark.eddard.smp.session.Session;
import com.lge.stark.eddard.smp.session.SessionNexus;
import com.lge.stark.eddard.smp.smpframe.ErrorInvalidSmpframeFormat;
import com.lge.stark.eddard.smp.smpframe.Smpframe;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import net.anyflow.menton.http.WebsocketFrameHandler;

public class SmpframeRouter extends WebsocketFrameHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SmpframeRouter.class);

	@Override
	public String subprotocols() {
		return Settings.SELF.getProperty("eddard.websocket.protocol");
	}

	@Override
	public String websocketPath() {
		return Settings.SELF.getProperty("eddard.websocket.path");
	}

	@Override
	public boolean allowExtensions() {
		return ALLOW_EXTENSIONS;
	}

	@Override
	public int maxFrameSize() {
		return MAX_FRAME_SIZE;
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		ctx.pipeline().addLast(new ConnectHandler());
		ctx.pipeline().addLast(new IsAliveHandler());
		ctx.pipeline().addLast(new CloseSessionHandler());
		ctx.pipeline().addLast(new RegisterDeviceHandler());
		ctx.pipeline().addLast(new DeleteDeviceHandler());
		ctx.pipeline().addLast(new UpdateDeviceStatusHandler());
	}

	@Override
	public void websocketFrameReceived(ChannelHandlerContext ctx, WebSocketFrame wsframe) {
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
		if (msg instanceof TextWebSocketFrame == false) {
			ctx.channel().writeAndFlush(new TextWebSocketFrame(new ErrorInvalidSmpframeFormat().toJsonString()))
					.addListener(ChannelFutureListener.CLOSE);
			return;
		}

		try {
			out.add(Smpframe.createFrom(((TextWebSocketFrame) msg).text()));
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			ctx.channel().writeAndFlush(new TextWebSocketFrame(new ErrorInvalidSmpframeFormat().toJsonString()))
					.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Session session = SessionNexus.SELF.getByChannel(ctx.channel());
		if (session == null) { return; }

		logger.error("===== Disposed session remoteAddress : "
				+ session.channelHandlerContext().channel().remoteAddress() + " =====");
		logger.error("===== Disposed session id : " + session.id() + " =====");

		SessionNexus.SELF.dispose(session);
	}
}