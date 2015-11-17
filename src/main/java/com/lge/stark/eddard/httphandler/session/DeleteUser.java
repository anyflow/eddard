package com.lge.stark.eddard.httphandler.session;

import io.netty.handler.codec.http.HttpResponseStatus;
import net.anyflow.menton.example.twitter.Database;
import net.anyflow.menton.example.twitter.MessageGenerator;
import net.anyflow.menton.example.twitter.model.Tweet;
import net.anyflow.menton.http.RequestHandler;

/**
 * @author Park Hyunjeong
 */
@RequestHandler.Handles(paths = { "session/{sessionId}/user/{userId}" }, httpMethods = { "DELETE" })
public class DeleteUser extends RequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeleteUser.class);

	@Override
	public String call() {
		String id = httpRequest().parameter("id");
		if(id == null) {
			httpResponse().setStatus(HttpResponseStatus.FORBIDDEN);
			return MessageGenerator.generateJson(new Error("Invalid id"), httpResponse());
		}

		Tweet tweet = Database.instance().get(id);
		if(tweet == null) {
			logger.error("id parameter required.");

			httpResponse().setStatus(HttpResponseStatus.FORBIDDEN);
			return MessageGenerator.generateJson(new Error("Invalid id"), httpResponse());
		}

		return MessageGenerator.generateJson(tweet, httpResponse());
	}
}