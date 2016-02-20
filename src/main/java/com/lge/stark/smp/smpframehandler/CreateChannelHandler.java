package com.lge.stark.smp.smpframehandler;

import com.lge.stark.controller.ChannelController;
import com.lge.stark.smp.session.Session;
import com.lge.stark.smp.smpframe.CreateChannel;
import com.lge.stark.smp.smpframe.ResCreateChannel;

import io.netty.channel.ChannelHandlerContext;

public class CreateChannelHandler extends SmpframeHandler<CreateChannel> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CreateChannelHandler.class);

	@Override
	protected void smpframeReceived(ChannelHandlerContext ctx, CreateChannel smpframe, Session session) throws Exception {

		ChannelController.ChannelMessage channelMessage = ChannelController.SELF.create(smpframe.name(), smpframe.secretKey(),
				smpframe.message(), smpframe.inviterId(), smpframe.inviteeIds().toArray(new String[0]));

		session.send(new ResCreateChannel(session.id(), smpframe.responseSmpframeId(), channelMessage.channel.getId(),
				channelMessage.message.getId(), channelMessage.channel.getCreateDate(), channelMessage.message.getUnreadCount()));
	}
}