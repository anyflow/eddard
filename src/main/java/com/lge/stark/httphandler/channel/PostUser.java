package com.lge.stark.httphandler.channel;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.inject.internal.Lists;
import com.lge.stark.FaultException;
import com.lge.stark.controller.ChannelController;
import com.lge.stark.model.Fault;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import net.anyflow.menton.http.HttpRequestHandler;

/**
 * @author Park Hyunjeong
 */
@HttpRequestHandler.Handles(paths = { "channel/{id}/user" }, httpMethods = { "POST" })
public class PostUser extends HttpRequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PostUser.class);

	@Override
	public String service() {
		String channelId = httpRequest().pathParameter("id");

		String content = httpRequest().content().toString(CharsetUtil.UTF_8);

		try {
			JSONObject json = new JSONObject(content);
			JSONArray usersJson = json.getJSONArray("users");

			List<String> userIds = Lists.newArrayList();
			for (int i = 0; i < usersJson.length(); ++i) {
				userIds.add(usersJson.get(i).toString());
			}

			return ChannelController.SELF.addUsers(channelId, userIds.toArray(new String[0])).toJsonString();
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