package com.lge.stark.smp.smpframehandler;

import com.lge.stark.controller.ChannelController;
import com.lge.stark.smp.session.Session;
import com.lge.stark.smp.smpframe.LeaveChannel;

import io.netty.channel.ChannelHandlerContext;

public class LeaveChannelHandler extends SmpframeHandler<LeaveChannel> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LeaveChannelHandler.class);

	@Override
	protected void smpframeReceived(ChannelHandlerContext ctx, LeaveChannel smpframe, Session session)
			throws Exception {

		ChannelController.SELF.leave(smpframe.channelId(), smpframe.userId());
	}
}