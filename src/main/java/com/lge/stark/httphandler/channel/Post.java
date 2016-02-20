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

@HttpRequestHandler.Handles(paths = { "channel" }, httpMethods = { "POST" })
public class Post extends HttpRequestHandler {

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

			ChannelController.ChannelMessage channelMessage = ChannelController.SELF.create(name, secretKey, message, inviterId,
					inviteeIds.toArray(new String[0]));

			JSONObject ret = new JSONObject();

			ret.put("id", channelMessage.channel.getId());
			ret.put("messageId", channelMessage.message.getId());
			ret.put("createDate", channelMessage.channel.getCreateDate().getTime());
			ret.put("unreadCount", channelMessage.message.getUnreadCount());

			return ret.toString();
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