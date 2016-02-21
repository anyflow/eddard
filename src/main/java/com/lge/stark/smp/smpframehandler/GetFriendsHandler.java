package com.lge.stark.smp.smpframehandler;

import java.util.ArrayList;
import java.util.List;

import com.lge.stark.gateway.UserGateway;
import com.lge.stark.model.User;
import com.lge.stark.smp.session.Session;
import com.lge.stark.smp.smpframe.GenericSmpframe;
import com.lge.stark.smp.smpframe.GetFriends;
import com.lge.stark.smp.smpframe.ModelResponse;
import com.lge.stark.smp.smpframe.OpCode;

import io.netty.channel.ChannelHandlerContext;

public class GetFriendsHandler extends SmpframeHandler<GetFriends> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GetFriendsHandler.class);

	@Override
	protected void smpframeReceived(ChannelHandlerContext ctx, GetFriends smpframe, Session session) throws Exception {
		List<User> users = UserGateway.SELF.getFriends(smpframe.userId());

		session.send(new GenericSmpframe<Users>(session.id(), smpframe.responseSmpframeId(), new Users(users)));
	}

	@SuppressWarnings("serial")
	@ModelResponse(name = "users", opcode = OpCode.RES_GET_FRIENDS)
	class Users extends ArrayList<User> {
		Users(List<User> users) {
			this.addAll(users);
		}
	}
}