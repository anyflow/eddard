package com.lge.stark.httphandler.message;

import org.json.JSONException;
import org.json.JSONObject;

import com.lge.stark.FaultException;
import com.lge.stark.controller.MessageController;
import com.lge.stark.model.Fault;
import com.lge.stark.model.Message;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import net.anyflow.menton.http.HttpRequestHandler;

@HttpRequestHandler.Handles(paths = { "message" }, httpMethods = { "POST" })
public class Post extends HttpRequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Post.class);

	@Override
	public String service() {
		String content = httpRequest().content().toString(CharsetUtil.UTF_8);

		try {
			JSONObject json = new JSONObject(content);

			String channelId = json.getString("channelId");
			String creatorId = json.getString("creatorId");
			String text = json.getString("text");

			Message msg = MessageController.SELF.create(channelId, text, creatorId);

			return msg.toJsonString();
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