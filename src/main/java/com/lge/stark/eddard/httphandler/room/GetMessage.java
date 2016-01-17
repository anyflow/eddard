package com.lge.stark.eddard.httphandler.room;

import java.util.List;

import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.Jsonizable;
import com.lge.stark.eddard.controller.MessageController;
import com.lge.stark.eddard.model.Fault;
import com.lge.stark.eddard.model.Message;

import io.netty.handler.codec.http.HttpResponseStatus;
import net.anyflow.menton.http.RequestHandler;

/**
 * @author Park Hyunjeong
 */
@RequestHandler.Handles(paths = { "room/{id}/message" }, httpMethods = { "GET" })
public class GetMessage extends RequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GetMessage.class);

	@Override
	public String service() {
		try {
			String id = httpRequest().pathParameter("id");

			if (id == null) {
				httpResponse().setStatus(HttpResponseStatus.BAD_REQUEST);
				return Fault.ROOM_001.replaceWith(id).toJsonString();
			}

			List<Message> ret = MessageController.SELF.getMessageAll(id);

			return Jsonizable.toJsonString(ret);
		}
		catch (FaultException fe) {
			logger.error(fe.getMessage(), fe);

			httpResponse().setStatus(fe.fault().httpResponseStatus());

			return fe.fault().toJsonString();
		}
	}
}