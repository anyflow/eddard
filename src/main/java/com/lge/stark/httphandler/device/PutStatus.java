package com.lge.stark.httphandler.device;

import org.json.JSONException;
import org.json.JSONObject;

import com.lge.stark.FaultException;
import com.lge.stark.controller.DeviceController;
import com.lge.stark.model.Fault;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import net.anyflow.menton.http.HttpRequestHandler;

@HttpRequestHandler.Handles(paths = { "device/{id}/status" }, httpMethods = { "PUT" })
public class PutStatus extends HttpRequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PutStatus.class);

	@Override
	public String service() {
		String content = httpRequest().content().toString(CharsetUtil.UTF_8);
		String deviceId = httpRequest().pathParameter("id");

		try {
			JSONObject json = new JSONObject(content);

			boolean isActive = json.getBoolean("isActive");

			DeviceController.SELF.updateStatus(deviceId, isActive);

			return null;
		}
		catch (FaultException fe) {
			logger.error(fe.getMessage(), fe);

			httpResponse().setStatus(fe.fault().httpResponseStatus());

			return fe.fault().toJsonString();
		}
		catch (JSONException e) {
			logger.error(e.getMessage(), e);

			httpResponse().setStatus(HttpResponseStatus.BAD_REQUEST);

			return Fault.COMMON_002.toJsonString();
		}
	}
}