package com.lge.stark.eddard.httphandler.device;

import org.json.JSONException;
import org.json.JSONObject;

import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.controller.DeviceController;
import com.lge.stark.eddard.model.Fault;
import com.lge.stark.eddard.model.PushType;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import net.anyflow.menton.http.RequestHandler;

@RequestHandler.Handles(paths = { "device" }, httpMethods = { "POST" })
public class Post extends RequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Post.class);

	@Override
	public String service() {
		String content = httpRequest().content().toString(CharsetUtil.UTF_8);

		try {
			JSONObject json = new JSONObject(content);

			String deviceId = json.getString("deviceId");
			String receiverId = json.getString("receiverId");
			PushType type = PushType.from(json.getString("type"));
			boolean isActive = json.getBoolean("isActive");

			DeviceController.SELF.create(deviceId, receiverId, type, isActive);

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