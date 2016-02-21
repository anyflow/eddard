package com.lge.stark.controller;

import java.util.Date;
import java.util.List;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.google.inject.internal.Lists;
import com.lge.stark.FaultException;
import com.lge.stark.IdGenerator;
import com.lge.stark.Jsonizable;
import com.lge.stark.gateway.ElasticsearchGateway;
import com.lge.stark.model.Channel;
import com.lge.stark.model.Fault;

public class ChannelController {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ChannelController.class);

	public final static ChannelController SELF;

	static {
		SELF = new ChannelController();
	}

	public Channel get(String id) throws FaultException {
		return get(ElasticsearchGateway.getClient(), id);
	}

	public Channel get(Client client, String id) throws FaultException {
		GetResponse response;
		try {
			response = client.prepareGet("stark", "channel", id).execute().actionGet();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		if (response.isExists() == false) { return null; }

		Channel ret = Jsonizable.read(response.getSourceAsString(), Channel.class);

		ret.setId(response.getId());

		return ret;
	}

	public Channel create(String name, String secretKey, String inviterId, String... inviteeIds) throws FaultException {
		return create(ElasticsearchGateway.getClient(), name, secretKey, inviterId, inviteeIds);
	}

	public Channel create(Client client, String name, String secretKey, String inviterId, String... inviteeIds)
			throws FaultException {
		List<String> users = Lists.newArrayList(inviteeIds);
		users.add(inviterId);

		Channel channel = new Channel();

		channel.setId(IdGenerator.newId());
		channel.setName(name);
		channel.setSecretKey(secretKey);
		channel.setCreateDate(new Date());
		channel.setUserIds(users);

		IndexResponse response;
		try {
			response = client.prepareIndex("stark", "channel").setSource(channel.toJsonStringWithout("id")).execute()
					.actionGet();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		if (response.isCreated() == false) { throw new FaultException(Fault.COMMON_000); }

		channel.setId(response.getId());

		return channel;
	}

	public Channel addUsers(String channelId, String... userIds) throws FaultException {
		return addUsers(ElasticsearchGateway.getClient(), channelId, userIds);
	}

	public Channel addUsers(Client client, String channelId, String... userIds) throws FaultException {
		Channel channel = get(client, channelId);
		channel.getUserIds().addAll(Lists.newArrayList(userIds));

		try {
			client.update(new UpdateRequest("stark", "channel", channelId)
					.doc(XContentFactory.jsonBuilder().startObject().field("users", channel.getUserIds()).endObject()))
					.get();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		return channel;
	}
}