package com.lge.stark.eddard.smp.smpframehandler;

import java.util.List;

import com.lge.stark.eddard.gateway.UserGateway;
import com.lge.stark.eddard.model.User;
import com.lge.stark.eddard.smp.session.Session;
import com.lge.stark.eddard.smp.smpframe.GetFriends;
import com.lge.stark.eddard.smp.smpframe.ResGetFriends;

import io.netty.channel.ChannelHandlerContext;

public class GetFriendsHandler extends SmpframeHandler<GetFriends> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GetFriendsHandler.class);

	@Override
	protected void smpframeReceived(ChannelHandlerContext ctx, GetFriends smpframe, Session session) throws Exception {
		List<User> users = UserGateway.SELF.getFriends(smpframe.userId());

		session.send(new ResGetFriends(session.id(), smpframe.responseSmpframeId(), users));
	}
}