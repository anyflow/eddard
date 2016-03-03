package com.lge.stark.controller;

import java.util.Date;
import java.util.List;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.internal.Lists;
import com.lge.stark.FaultException;
import com.lge.stark.IdGenerator;
import com.lge.stark.Jsonizable;
import com.lge.stark.gateway.ElasticsearchGateway;
import com.lge.stark.gateway.PushGateway;
import com.lge.stark.gateway.UserGateway;
import com.lge.stark.model.Channel;
import com.lge.stark.model.Device;
import com.lge.stark.model.Fault;
import com.lge.stark.smp.session.Session;
import com.lge.stark.smp.session.SessionNexus;
import com.lge.stark.smp.smpframe.OpCode;
import com.lge.stark.smp.smpframe.Smpframe;

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

		// user validation
		UserGateway.SELF.get(inviteeIds);
		UserGateway.SELF.get(inviterId);

		final Channel channel = new Channel();

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

		broadcast(channel, new Smpframe(OpCode.CHANNEL_CREATED) {
			@JsonProperty("channel")
			Channel item = channel;

			@Override
			public boolean isResponseRequired() {
				return false;
			}
		});

		return channel;
	}

	public Channel addUsers(String id, String... userIds) throws FaultException {
		return addUsers(ElasticsearchGateway.getClient(), id, userIds);
	}

	public Channel addUsers(Client client, String id, String... userIds) throws FaultException {
		Channel channel = get(client, id);
		channel.getUserIds().addAll(Lists.newArrayList(userIds));

		try {
			client.update(new UpdateRequest("stark", "channel", id).doc(
					XContentFactory.jsonBuilder().startObject().field("userIds", channel.getUserIds()).endObject()))
					.get();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		return channel;
	}

	public List<Device> broadcast(String id, Smpframe smpframe) throws FaultException {
		Channel channel = get(id);

		if (channel == null) {
			logger.error("Channel({}) is not found!", id);
			return null;
		}

		return broadcast(channel, smpframe);
	}

	public List<Device> broadcast(Channel channel, Smpframe smpframe) throws FaultException {
		List<Device> ret = Lists.newArrayList();

		for (String uid : channel.getUserIds()) {
			Device device = DeviceController.SELF.getActive(uid);
			if (device == null) {
				continue;
			}

			ret.add(device);

			Session session = SessionNexus.SELF.getByDeviceId(device.getId());
			if (session == null) {
				PushGateway.SELF.send(device.getType(), device.getReceiverId(), smpframe);
				continue;
			}

			smpframe.sessionId(session.id());
			smpframe.id(session.nextPushframeId());

			session.send(smpframe);
		}

		return ret;
	}

	public void leave(String id, final String userId) throws FaultException {
		Client client = ElasticsearchGateway.getClient();

		final Channel channel = get(id);

		if (channel == null) {
			logger.error("Channel({}) is not found!", id);
			return;
		}

		broadcast(channel, new Smpframe(OpCode.CHANNEL_LEFT) {
			@JsonProperty("channelId")
			String channelId = channel.getId();

			@JsonProperty("userId")
			String uId = userId;

			@Override
			public boolean isResponseRequired() {
				// TODO Auto-generated method stub
				return false;
			}
		});

		try {
			if (channel.getUserIds().size() > 0) {
				client.update(new UpdateRequest("stark", "channel", id).doc(channel.toJsonString())).get();
			}
			else {
				client.prepareDelete("stark", "channel", id).get();
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}
	}
}