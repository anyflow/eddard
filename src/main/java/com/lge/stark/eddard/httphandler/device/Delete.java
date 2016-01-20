package com.lge.stark.eddard.httphandler.device;

import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.controller.DeviceController;

import net.anyflow.menton.http.HttpRequestHandler;

@HttpRequestHandler.Handles(paths = { "device/{id}" }, httpMethods = { "DELETE" })
public class Delete extends HttpRequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Delete.class);

	@Override
	public String service() {

		try {
			String id = httpRequest().pathParameter("id");
			DeviceController.SELF.delete(id);

			return null;
		}
		catch (FaultException fe) {
			logger.error(fe.getMessage(), fe);

			httpResponse().setStatus(fe.fault().httpResponseStatus());

			return fe.fault().toJsonString();
		}
	}
}