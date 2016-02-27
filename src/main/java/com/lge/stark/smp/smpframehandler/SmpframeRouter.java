package com.lge.stark.smp.smpframehandler;

import java.util.List;

import com.lge.stark.Settings;
import com.lge.stark.smp.session.Session;
import com.lge.stark.smp.session.SessionNexus;
import com.lge.stark.smp.smpframe.OpCode;
import com.lge.stark.smp.smpframe.Smpframe;

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
		ctx.pipeline().addLast(new InitializeHandler());
		ctx.pipeline().addLast(new IsAliveHandler());
		ctx.pipeline().addLast(new CloseSessionHandler());
		ctx.pipeline().addLast(new RegisterDeviceHandler());
		ctx.pipeline().addLast(new DeleteDeviceHandler());
		ctx.pipeline().addLast(new UpdateDeviceStatusHandler());
		ctx.pipeline().addLast(new RetrieveFriendsHandler());
		ctx.pipeline().addLast(new CreateChannelHandler());
		ctx.pipeline().addLast(new CreateMessageHandler());
		ctx.pipeline().addLast(new LeaveChannelHandler());
	}

	@Override
	public void websocketFrameReceived(ChannelHandlerContext ctx, WebSocketFrame wsframe) {
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {

		Smpframe errorInvalidSmpframeFormat = new Smpframe(OpCode.ERROR_INVALID_SMPFRAME_FORMAT, null, -1) {
			@Override
			public boolean isResponseRequired() {
				return false;
			}
		};

		if (msg instanceof TextWebSocketFrame == false) {
			ctx.channel().writeAndFlush(new TextWebSocketFrame(errorInvalidSmpframeFormat.toJsonString()))
					.addListener(ChannelFutureListener.CLOSE);
			return;
		}

		TextWebSocketFrame frame = (TextWebSocketFrame) msg;
		logger.debug("textFrame received : {}", frame.text());

		try {
			out.add(Smpframe.createFrom(frame.text()));
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			ctx.channel().writeAndFlush(new TextWebSocketFrame(errorInvalidSmpframeFormat.toJsonString()))
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