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

@RequestHandler.Handles(paths = { "room" }, httpMethods = { "POST" })
public class Post extends RequestHandler {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Post.class);

	@Override
	public String service() {
		String content = httpRequest().content().toString(CharsetUtil.UTF_8);

		try {
			JSONObject json = new JSONObject(content);

			String name = json.getString("name");
			String inviterId = json.getString("inviterId");
			JSONArray inviteeIdsJson = json.getJSONArray("inviteeIds");
			String secretKey = json.getString("secretKey");
			String message = json.getString("message");

			List<String> inviteeIds = Lists.newArrayList();
			for (int i = 0; i < inviteeIdsJson.length(); ++i) {
				inviteeIds.add(inviteeIdsJson.get(i).toString());
			}

			RoomController.RoomMessage roomMessage = RoomController.SELF.create(name, inviterId, inviteeIds, secretKey,
					message);

			JSONObject ret = new JSONObject();

			ret.put("id", roomMessage.room.getId());
			ret.put("messageId", roomMessage.message.getId());
			ret.put("createDate", roomMessage.room.getCreateDate().getTime());
			ret.put("unreadCount", roomMessage.message.getUnreadCount());

			return ret.toString();
		}
		catch (FaultException fe) {
			logger.error(fe.getMessage(), fe);

			httpResponse().setStatus(fe.fault().httpStatus());

			return fe.fault().toJsonString();
		}
		catch (JSONException e) {
			logger.error(e.getMessage(), e);

			httpResponse().setStatus(HttpResponseStatus.BAD_REQUEST);

			return (new Fault("1", "Invalid JSON content")).toJsonString();
		}
	}
}