package com.lge.stark.eddard.controller.session;

import net.anyflow.menton.http.RequestHandler;

/**
 * @author Park Hyunjeong
 */
@RequestHandler.Handles(paths = { "session/{sessionId}/user/{userId}" }, httpMethods = { "DELETE" })
public class DeleteUser extends RequestHandler {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeleteUser.class);

	@Override
	public String service() {
		String sessionId = httpRequest().pathParameter("sessionId");
		String userId = httpRequest().pathParameter("userId");

		return sessionId + userId;
	}
}