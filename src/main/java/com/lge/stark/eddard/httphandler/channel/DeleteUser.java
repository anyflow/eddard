package com.lge.stark.eddard.httphandler.channel;

import net.anyflow.menton.http.HttpRequestHandler;

/**
 * @author Park Hyunjeong
 */
@HttpRequestHandler.Handles(paths = { "channel/{id}/user/{userId}" }, httpMethods = { "DELETE" })
public class DeleteUser extends HttpRequestHandler {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeleteUser.class);

	@Override
	public String service() {
		String channelId = httpRequest().pathParameter("id");
		String userId = httpRequest().pathParameter("userId");

		return channelId + userId;
	}
}