package com.lge.stark.eddard.httphandler.room;

import com.lge.stark.eddard.Fault;
import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.controller.RoomController;

import io.netty.handler.codec.http.HttpResponseStatus;
import net.anyflow.menton.http.RequestHandler;

/**
 * @author Park Hyunjeong
 */
@RequestHandler.Handles(paths = { "room/{id}" }, httpMethods = { "GET" })
public class Get extends RequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Get.class);

	@Override
	public String service() {
		try {
			String id = httpRequest().pathParameter("id");

			if (id == null) {
				httpResponse().setStatus(HttpResponseStatus.BAD_REQUEST);
				return (new Fault("1", "Invalid ID")).toJsonString();
			}

			return RoomController.instance().get(id).toJsonString();
		}
		catch (FaultException fe) {
			logger.error(fe.getMessage(), fe);

			httpResponse().setStatus(fe.fault().httpStatus());

			return fe.fault().getMessage();
		}
	}
}