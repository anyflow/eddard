package com.lge.stark.controller;

import java.util.Date;
import java.util.List;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.google.common.collect.Lists;
import com.lge.stark.FaultException;
import com.lge.stark.Jsonizable;
import com.lge.stark.gateway.ElasticsearchGateway;
import com.lge.stark.gateway.PushGateway;
import com.lge.stark.mockserver.ProfileServer;
import com.lge.stark.model.Channel;
import com.lge.stark.model.Device;
import com.lge.stark.model.Fault;
import com.lge.stark.model.Message;
import com.lge.stark.model.MessageStatus;

public class MessageController {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MessageController.class);

	public final static MessageController SELF;

	static {
		SELF = new MessageController();
	}

	public Message create(String channelId, String text, String creatorId) throws FaultException {

		Channel channel = ChannelController.SELF.get(channelId);

		if (channel == null) { throw new FaultException(Fault.CHANNEL_001); }

		Message ret = new Message();

		ret.setCreateDate(new Date());
		ret.setCreatorId(creatorId);
		ret.setText(text);
		ret.setChannelId(channelId);
		ret.setUnreadCount(channel.getUserIds().size() - 1);

		Client client = ElasticsearchGateway.getClient();

		IndexResponse response;
		try {
			response = client.prepareIndex("stark", "message").setSource(ret.toJsonStringWithout("id")).execute()
					.actionGet();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		if (response.isCreated() == false) { throw new FaultException(Fault.COMMON_000); }

		ret.setId(response.getId());

		for (String inviteeId : channel.getUserIds()) {
			if (inviteeId.equals(creatorId)) {
				continue;
			}

			List<String> deviceIds = ProfileServer.SELF.getDeviceIds(inviteeId);

			List<Device> devices = DeviceController.SELF.get(deviceIds.toArray(new String[0]));

			if (devices == null || devices.size() <= 0) {
				continue;
			}

			Device activeDevice = devices.stream().filter(item -> {
				return item.isActive();
			}).findFirst().orElse(null);

			if (activeDevice == null) {
				continue;
			}

			PushGateway.SELF.send(activeDevice.getType(), activeDevice.getReceiverId(), ret);

			MessageStatus ms = new MessageStatus();

			ms.setMessageId(ret.getId());
			ms.setDeviceId(activeDevice.getId());
			ms.setStatus(MessageStatus.Status.PEND);
			ms.setCreateDate(new Date());

			response = client.prepareIndex("stark", "messageStatus", ms.getId()).setSource(ms.toJsonStringWithout("id"))
					.execute().actionGet();

			if (response.isCreated() == false) { throw new FaultException(Fault.COMMON_000); }
		}

		return ret;
	}

	public void setUnreadCount(String messageId, int unreadCount) throws FaultException {
		Client client = ElasticsearchGateway.getClient();

		try {
			client.update(new UpdateRequest("stark", "message", messageId)
					.doc(XContentFactory.jsonBuilder().startObject().field("unreadCount", unreadCount).endObject()))
					.get();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}
	}

	public List<Message> getMessageAll(String id) throws FaultException {
		SearchResponse response;
		try {
			response = ElasticsearchGateway.getClient().prepareSearch("stark").setTypes("message")
					.setQuery(QueryBuilders.matchQuery("channelId", id)).execute().actionGet();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		List<Message> ret = Lists.newArrayList();

		logger.debug("hits : {}", response.getHits().getTotalHits());

		if (response.getHits().getTotalHits() <= 0) { return ret; }

		for (SearchHit hit : response.getHits().hits()) {

			Message item = Jsonizable.read(hit.getSourceAsString(), Message.class);
			item.setId(hit.getId());

			logger.debug(item.toJsonString());

			ret.add(item);
		}

		return ret;
	}
}