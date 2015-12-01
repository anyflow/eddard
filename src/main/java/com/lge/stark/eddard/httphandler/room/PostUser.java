package com.lge.stark.eddard.httphandler.room;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.inject.internal.Lists;
import com.lge.stark.eddard.Fault;
import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.controller.RoomController;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import net.anyflow.menton.http.RequestHandler;

/**
 * @author Park Hyunjeong
 */
@RequestHandler.Handles(paths = { "room/{id}/user" }, httpMethods = { "POST" })
public class PostUser extends RequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PostUser.class);

	@Override
	public String service() {
		String roomId = httpRequest().pathParameter("id");

		String content = httpRequest().content().toString(CharsetUtil.UTF_8);

		try {
			JSONObject json = new JSONObject(content);
			JSONArray usersJson = json.getJSONArray("users");

			List<String> userIds = Lists.newArrayList();
			for (int i = 0; i < usersJson.length(); ++i) {
				userIds.add(usersJson.get(i).toString());
			}

			return RoomController.instance().addUsers(roomId, userIds).toJsonString();
		}
		catch (FaultException fe) {
			logger.error(fe.getMessage(), fe);

			httpResponse().setStatus(fe.fault().httpStatus());

			return fe.fault().getMessage();
		}
		catch (JSONException e) {
			logger.error(e.getMessage(), e);

			httpResponse().setStatus(HttpResponseStatus.BAD_REQUEST);

			return (new Fault("1", "Invalid JSON content")).toJsonString();
		}
	}
}