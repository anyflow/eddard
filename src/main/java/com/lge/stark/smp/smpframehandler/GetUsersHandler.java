package com.lge.stark.smp.smpframehandler;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lge.stark.FaultException;
import com.lge.stark.gateway.UserGateway;
import com.lge.stark.model.User;
import com.lge.stark.smp.session.Session;
import com.lge.stark.smp.smpframe.GetUsers;
import com.lge.stark.smp.smpframe.OpCode;
import com.lge.stark.smp.smpframe.Smpframe;

import io.netty.channel.ChannelHandlerContext;

public class GetUsersHandler extends SmpframeHandler<GetUsers> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GetUsersHandler.class);

	@Override
	protected void smpframeReceived(ChannelHandlerContext ctx, GetUsers smpframe, Session session)
			throws FaultException {
		final List<User> users = UserGateway.SELF.get(smpframe.userIds().toArray(new String[0]));

		session.send(new Smpframe(OpCode.USERS_RETRIEVED, null, smpframe.responseSmpframeId()) {
			@JsonProperty("users")
			Users item = new Users(users);
		});
	}

	@SuppressWarnings("serial")
	class Users extends ArrayList<User> {
		Users(List<User> users) {
			this.addAll(users);
		}
	}
}