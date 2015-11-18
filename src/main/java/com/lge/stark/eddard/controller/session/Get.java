package com.lge.stark.eddard.controller.session;

import com.lge.stark.eddard.model.Fault;
import com.lge.stark.eddard.model.Session;
import com.lge.stark.eddard.model.business.SessionBiz;

import io.netty.handler.codec.http.HttpResponseStatus;
import net.anyflow.menton.http.RequestHandler;

/**
 * @author Park Hyunjeong
 */
@RequestHandler.Handles(paths = { "session/{sessionId}" }, httpMethods = { "GET" })
public class Get extends RequestHandler {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Get.class);

	@Override
	public String call() {
		String id = httpRequest().pathParameter("sessionId");

		if (id == null) {
			httpResponse().setStatus(HttpResponseStatus.BAD_REQUEST);
			return (new Fault("1", "Invalid ID")).toJsonString();
		}

		Session session = SessionBiz.instance().getSession(id);

		if (session == null) {
			httpResponse().setStatus(HttpResponseStatus.BAD_REQUEST);
			return (new Fault("2", "Session not found.")).toJsonString();
		}

		return session.toJsonString();
	}
}