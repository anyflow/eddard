package com.lge.stark.eddard.controller.room;

import com.lge.stark.eddard.Fault;
import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.business.RoomBiz;

import io.netty.handler.codec.http.HttpResponseStatus;
import net.anyflow.menton.http.RequestHandler;

/**
 * @author Park Hyunjeong
 */
@RequestHandler.Handles(paths = { "room/{roomId}" }, httpMethods = { "GET" })
public class Get extends RequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Get.class);

	@Override
	public String service() {
		try {
			String id = httpRequest().pathParameter("roomId");

			if (id == null) {
				httpResponse().setStatus(HttpResponseStatus.BAD_REQUEST);
				return (new Fault("1", "Invalid ID")).toJsonString();
			}

			return RoomBiz.instance().get(id).toJsonString();
		}
		catch (FaultException fe) {
			logger.error(fe.getMessage(), fe);

			httpResponse().setStatus(fe.fault().httpStatus());

			return fe.fault().getMessage();
		}
	}
}