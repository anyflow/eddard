package com.lge.stark.smp.smpframehandler;

import com.lge.stark.controller.ChannelController;
import com.lge.stark.model.Channel;
import com.lge.stark.smp.session.Session;
import com.lge.stark.smp.smpframe.CreateChannel;
import com.lge.stark.smp.smpframe.GenericSmpframe;

import io.netty.channel.ChannelHandlerContext;

public class CreateChannelHandler extends SmpframeHandler<CreateChannel> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CreateChannelHandler.class);

	@Override
	protected void smpframeReceived(ChannelHandlerContext ctx, CreateChannel smpframe, Session session)
			throws Exception {

		Channel channel = ChannelController.SELF.create(smpframe.name(), smpframe.secretKey(), smpframe.inviterId(),
				smpframe.inviteeIds().toArray(new String[0]));

		session.send(new GenericSmpframe<Channel>(session.id(), smpframe.responseSmpframeId(), channel));
	}
}