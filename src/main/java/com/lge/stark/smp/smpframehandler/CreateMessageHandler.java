package com.lge.stark.smp.smpframehandler;

import com.lge.stark.controller.MessageController;
import com.lge.stark.smp.session.Session;
import com.lge.stark.smp.smpframe.CreateMessage;

import io.netty.channel.ChannelHandlerContext;

public class CreateMessageHandler extends SmpframeHandler<CreateMessage> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CreateMessageHandler.class);

	@Override
	protected void smpframeReceived(ChannelHandlerContext ctx, CreateMessage smpframe, Session session)
			throws Exception {

		MessageController.SELF.create(smpframe.channelId(), smpframe.text(), smpframe.creatorId());
	}
}