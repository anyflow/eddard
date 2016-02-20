package com.lge.stark.eddard.smp.smpframehandler;

import com.lge.stark.eddard.controller.ChannelController;
import com.lge.stark.eddard.smp.session.Session;
import com.lge.stark.eddard.smp.smpframe.CreateRoom;
import com.lge.stark.eddard.smp.smpframe.ResCreateRoom;

import io.netty.channel.ChannelHandlerContext;

public class CreateRoomHandler extends SmpframeHandler<CreateRoom> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CreateRoomHandler.class);

	@Override
	protected void smpframeReceived(ChannelHandlerContext ctx, CreateRoom smpframe, Session session) throws Exception {

		ChannelController.ChannelMessage roomMessage = ChannelController.SELF.create(smpframe.name(), smpframe.secretKey(),
				smpframe.message(), smpframe.inviterId(), smpframe.inviteeIds().toArray(new String[0]));

		session.send(new ResCreateRoom(session.id(), smpframe.responseSmpframeId(), roomMessage.channel.getId(),
				roomMessage.message.getId(), roomMessage.channel.getCreateDate(), roomMessage.message.getUnreadCount()));
	}
}