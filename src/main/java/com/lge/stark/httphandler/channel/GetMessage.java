package com.lge.stark.httphandler.channel;

import java.util.List;

import com.lge.stark.FaultException;
import com.lge.stark.Jsonizable;
import com.lge.stark.controller.MessageController;
import com.lge.stark.model.Fault;
import com.lge.stark.model.Message;

import io.netty.handler.codec.http.HttpResponseStatus;
import net.anyflow.menton.http.HttpRequestHandler;

/**
 * @author Park Hyunjeong
 */
@HttpRequestHandler.Handles(paths = { "channel/{id}/messages" }, httpMethods = { "GET" })
public class GetMessage extends HttpRequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GetMessage.class);

	@Override
	public String service() {
		try {
			String id = httpRequest().pathParameter("id");

			if (id == null) {
				httpResponse().setStatus(HttpResponseStatus.BAD_REQUEST);
				return Fault.CHANNEL_001.replaceWith(id).toJsonString();
			}

			List<Message> ret = MessageController.SELF.getMessages(id);

			return Jsonizable.toJsonString(ret);
		}
		catch (FaultException fe) {
			logger.error(fe.getMessage(), fe);

			httpResponse().setStatus(fe.fault().httpResponseStatus());

			return fe.fault().toJsonString();
		}
	}
}