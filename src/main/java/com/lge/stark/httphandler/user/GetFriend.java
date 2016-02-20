package com.lge.stark.httphandler.user;

import java.util.List;

import com.lge.stark.FaultException;
import com.lge.stark.Jsonizable;
import com.lge.stark.gateway.UserGateway;
import com.lge.stark.model.User;

import net.anyflow.menton.http.HttpRequestHandler;

/**
 * @author Park Hyunjeong
 */
@HttpRequestHandler.Handles(paths = { "user/{id}/friend" }, httpMethods = { "GET" })
public class GetFriend extends HttpRequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GetFriend.class);

	@Override
	public String service() {
		String userId = httpRequest().pathParameter("id");

		try {
			List<User> users = UserGateway.SELF.getFriends(userId);

			return Jsonizable.toJsonString(users);
		}
		catch (FaultException fe) {
			logger.error(fe.getMessage(), fe);

			httpResponse().setStatus(fe.fault().httpResponseStatus());

			return fe.fault().toJsonString();
		}
	}
}