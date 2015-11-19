package com.lge.stark.eddard.controller.room;

import net.anyflow.menton.http.RequestHandler;

/**
 * @author Park Hyunjeong
 */
@RequestHandler.Handles(paths = { "room/{roomId}/user/{userId}" }, httpMethods = { "DELETE" })
public class DeleteUser extends RequestHandler {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeleteUser.class);

	@Override
	public String service() {
		String roomId = httpRequest().pathParameter("roomId");
		String userId = httpRequest().pathParameter("userId");

		return roomId + userId;
	}
}