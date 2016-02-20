package com.lge.stark.eddard.controller;

import java.util.Date;
import java.util.List;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.google.inject.internal.Lists;
import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.IdGenerator;
import com.lge.stark.eddard.Jsonizable;
import com.lge.stark.eddard.gateway.ElasticsearchGateway;
import com.lge.stark.eddard.gateway.PushGateway;
import com.lge.stark.eddard.mockserver.ProfileServer;
import com.lge.stark.eddard.model.Channel;
import com.lge.stark.eddard.model.Device;
import com.lge.stark.eddard.model.Fault;
import com.lge.stark.eddard.model.Message;
import com.lge.stark.eddard.model.MessageStatus;

public class ChannelController {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ChannelController.class);

	public final static ChannelController SELF;

	static {
		SELF = new ChannelController();
	}

	public class ChannelMessage {
		public Channel channel;
		public Message message;

		protected ChannelMessage(Channel channel, Message message) {
			this.channel = channel;
			this.message = message;
		}
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

	public ChannelMessage create(String name, String secretKey, String message, String inviterId, String... inviteeIds)
			throws FaultException {
		return create(ElasticsearchGateway.getClient(), name, secretKey, message, inviterId, inviteeIds);
	}

	public ChannelMessage create(Client client, String name, String secretKey, String message, String inviterId,
			String... inviteeIds) throws FaultException {
		List<String> users = Lists.newArrayList(inviteeIds);
		users.add(inviterId);

		Channel channel = new Channel();

		channel.setId(IdGenerator.newId());
		channel.setName(name);
		channel.setSecretKey(secretKey);
		channel.setCreateDate(new Date());
		channel.setUsers(users);

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

		Message msg = MessageController.SELF.create(channel.getId(), message, inviterId);

		for (String inviteeId : inviteeIds) {
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

			PushGateway.SELF.send(activeDevice.getType(), activeDevice.getReceiverId(), message);

			MessageStatus ms = new MessageStatus();

			ms.setMessageId(msg.getId());
			ms.setDeviceId(activeDevice.getId());
			ms.setStatus(MessageStatus.Status.PEND);
			ms.setCreateDate(new Date());

			response = client.prepareIndex("stark", "messageStatus", ms.getId()).setSource(ms.toJsonStringWithout("id"))
					.execute().actionGet();

			if (response.isCreated() == false) { throw new FaultException(Fault.COMMON_000); }

			MessageController.SELF.setUnreadCount(msg.getId(), msg.getUnreadCount() + 1);
		}

		return new ChannelMessage(channel, msg);
	}

	public Channel addUsers(String channelId, String... userIds) throws FaultException {
		return addUsers(ElasticsearchGateway.getClient(), channelId, userIds);
	}

	public Channel addUsers(Client client, String channelId, String... userIds) throws FaultException {
		Channel channel = get(client, channelId);
		channel.getUsers().addAll(Lists.newArrayList(userIds));

		try {
			client.update(new UpdateRequest("stark", "channel", channelId)
					.doc(XContentFactory.jsonBuilder().startObject().field("users", channel.getUsers()).endObject()))
					.get();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		return channel;
	}
}