package com.lge.stark.eddard.httphandler.channel;

import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.controller.ChannelController;
import com.lge.stark.eddard.model.Fault;

import io.netty.handler.codec.http.HttpResponseStatus;
import net.anyflow.menton.http.HttpRequestHandler;

/**
 * @author Park Hyunjeong
 */
@HttpRequestHandler.Handles(paths = { "channel/{id}" }, httpMethods = { "GET" })
public class Get extends HttpRequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Get.class);

	@Override
	public String service() {
		try {
			String id = httpRequest().pathParameter("id");

			if (id == null) {
				httpResponse().setStatus(HttpResponseStatus.BAD_REQUEST);
				return Fault.CHANNEL_001.replaceWith(id).toJsonString();
			}

			return ChannelController.SELF.get(id).toJsonString();
		}
		catch (FaultException fe) {
			logger.error(fe.getMessage(), fe);

			httpResponse().setStatus(fe.fault().httpResponseStatus());

			return fe.fault().toJsonString();
		}
	}
}