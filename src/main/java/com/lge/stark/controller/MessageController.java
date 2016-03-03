package com.lge.stark.controller;

import java.util.Date;
import java.util.List;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.lge.stark.FaultException;
import com.lge.stark.Jsonizable;
import com.lge.stark.gateway.ElasticsearchGateway;
import com.lge.stark.model.Channel;
import com.lge.stark.model.Device;
import com.lge.stark.model.Fault;
import com.lge.stark.model.Message;
import com.lge.stark.model.MessageStatus.Status;
import com.lge.stark.smp.smpframe.OpCode;
import com.lge.stark.smp.smpframe.Smpframe;

public class MessageController {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MessageController.class);

	public final static MessageController SELF;

	static {
		SELF = new MessageController();
	}

	public Message create(String channelId, String text, String creatorId) throws FaultException {
		Channel channel = ChannelController.SELF.get(channelId);

		if (channel == null) { throw new FaultException(Fault.CHANNEL_001); }

		final Message ret = new Message();

		ret.setCreateDate(new Date());
		ret.setCreatorId(creatorId);
		ret.setText(text);
		ret.setChannelId(channelId);
		ret.setUnreadCount(channel.getUserIds().size());

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

		List<Device> devices = ChannelController.SELF.broadcast(channel, new Smpframe(OpCode.MESSAGE_CREATED) {
			@JsonProperty("message")
			Message item = ret;

			@Override
			public boolean isResponseRequired() {
				return false;
			}
		});

		for (Device device : devices) {
			MessageStatusController.SELF.create(client, ret.getId(), device.getId(), Status.SENT);
		}

		return ret;
	}

	public Message get(String id) throws FaultException {
		Client client = ElasticsearchGateway.getClient();

		return get(client, id);
	}

	public Message get(Client client, String id) throws FaultException {
		GetResponse response;
		try {
			response = client.prepareGet("stark", "message", id).execute().actionGet();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		if (response.isExists() == false) { return null; }

		Message ret = Jsonizable.read(response.getSourceAsString(), Message.class);
		ret.setId(id);

		return ret;
	}

	public void update(Client client, Message message) throws FaultException {
		try {
			client.update(new UpdateRequest("stark", "message", message.getId()).doc(message.toJsonString())).get();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}
	}

	public List<Message> getMessages(String channelId) throws FaultException {
		SearchResponse response;
		try {
			response = ElasticsearchGateway.getClient().prepareSearch("stark").setTypes("message")
					.setQuery(QueryBuilders.matchQuery("channelId", channelId)).execute().actionGet();
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