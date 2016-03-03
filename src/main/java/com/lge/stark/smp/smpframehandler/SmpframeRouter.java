package com.lge.stark.smp.smpframehandler;

import java.util.List;

import com.lge.stark.FaultException;
import com.lge.stark.Settings;
import com.lge.stark.controller.ChannelController;
import com.lge.stark.controller.DeviceController;
import com.lge.stark.controller.MessageController;
import com.lge.stark.smp.session.Session;
import com.lge.stark.smp.session.SessionNexus;
import com.lge.stark.smp.smpframe.CloseSession;
import com.lge.stark.smp.smpframe.CreateChannel;
import com.lge.stark.smp.smpframe.CreateMessage;
import com.lge.stark.smp.smpframe.DeleteDevice;
import com.lge.stark.smp.smpframe.IsAlive;
import com.lge.stark.smp.smpframe.LeaveChannel;
import com.lge.stark.smp.smpframe.OpCode;
import com.lge.stark.smp.smpframe.RegisterDevice;
import com.lge.stark.smp.smpframe.ReturnOk;
import com.lge.stark.smp.smpframe.Smpframe;
import com.lge.stark.smp.smpframe.UpdateDeviceStatus;

import io.netty.channel.ChannelFuture;
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
	public void websocketFrameReceived(ChannelHandlerContext ctx, WebSocketFrame wsframe) {
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {

		if (msg instanceof TextWebSocketFrame == false) {
			ctx.channel().writeAndFlush(new TextWebSocketFrame((new Smpframe(OpCode.ERROR_INVALID_SMPFRAME_FORMAT) {
			}).toJsonString())).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					logger.debug("None session sending finished. smpframe - {}", msg.toString());
				}
			}).addListener(ChannelFutureListener.CLOSE);
			return;
		}

		TextWebSocketFrame frame = (TextWebSocketFrame) msg;
		logger.debug("textFrame received : {}", frame.text());

		try {
			out.add(Smpframe.createFrom(frame.text()));
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			ctx.channel().writeAndFlush(new TextWebSocketFrame((new Smpframe(OpCode.ERROR_INVALID_SMPFRAME_FORMAT) {
			}).toJsonString())).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					logger.debug("None session sending finished. smpframe - {}", msg.toString());
				}
			}).addListener(ChannelFutureListener.CLOSE);
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

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		ctx.pipeline().addLast(new InitializeHandler());
		ctx.pipeline().addLast(new RetrieveFriendsHandler());
		ctx.pipeline().addLast(new GetUsersHandler());
		ctx.pipeline().addLast(new MessageReceivedHandler());

		ctx.pipeline().addLast(new SmpframeHandler<IsAlive>() {
			@Override
			protected void smpframeReceived(ChannelHandlerContext ctx, IsAlive smpframe, Session session)
					throws FaultException {
				session.send(new ReturnOk(session.id(), smpframe.responseSmpframeId()));
			}
		});
		ctx.pipeline().addLast(new SmpframeHandler<CloseSession>() {
			@Override
			protected void smpframeReceived(ChannelHandlerContext ctx, CloseSession smpframe, Session session)
					throws FaultException {
				session.send(new ReturnOk(session.id(), smpframe.responseSmpframeId()));
				SessionNexus.SELF.dispose(session);
			}

		});
		ctx.pipeline().addLast(new SmpframeHandler<RegisterDevice>() {
			@Override
			protected void smpframeReceived(ChannelHandlerContext ctx, RegisterDevice smpframe, Session session)
					throws FaultException {
				DeviceController.SELF.create(smpframe.deviceId(), smpframe.receiverId(), smpframe.pushType(),
						smpframe.isActive());

				session.send(new ReturnOk(session.id(), smpframe.responseSmpframeId()));
			}
		});
		ctx.pipeline().addLast(new SmpframeHandler<DeleteDevice>() {
			@Override
			protected void smpframeReceived(ChannelHandlerContext ctx, DeleteDevice smpframe, Session session)
					throws FaultException {
				DeviceController.SELF.delete(smpframe.deviceId());

				session.send(new ReturnOk(session.id(), smpframe.responseSmpframeId()));
			}
		});
		ctx.pipeline().addLast(new SmpframeHandler<UpdateDeviceStatus>() {
			@Override
			protected void smpframeReceived(ChannelHandlerContext ctx, UpdateDeviceStatus smpframe, Session session)
					throws FaultException {
				DeviceController.SELF.updateStatus(smpframe.deviceId(), smpframe.isActive());

				session.send(new ReturnOk(session.id(), smpframe.responseSmpframeId()));
			}
		});
		ctx.pipeline().addLast(new SmpframeHandler<CreateChannel>() {
			@Override
			protected void smpframeReceived(ChannelHandlerContext ctx, CreateChannel smpframe, Session session)
					throws FaultException {
				CreateChannel cc = smpframe;
				ChannelController.SELF.create(cc.name(), cc.secretKey(), cc.inviterId(),
						cc.inviteeIds().toArray(new String[0]));
			}
		});
		ctx.pipeline().addLast(new SmpframeHandler<CreateMessage>() {
			@Override
			protected void smpframeReceived(ChannelHandlerContext ctx, CreateMessage smpframe, Session session)
					throws FaultException {
				MessageController.SELF.create(smpframe.channelId(), smpframe.text(), smpframe.creatorId());
			}
		});
		ctx.pipeline().addLast(new SmpframeHandler<LeaveChannel>() {
			@Override
			protected void smpframeReceived(ChannelHandlerContext ctx, LeaveChannel smpframe, Session session)
					throws FaultException {
				ChannelController.SELF.leave(smpframe.channelId(), smpframe.userId());
			}
		});
	}
}