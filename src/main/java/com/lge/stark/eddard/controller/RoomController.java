package com.lge.stark.eddard.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
import com.lge.stark.eddard.model.Device;
import com.lge.stark.eddard.model.Fault;
import com.lge.stark.eddard.model.Message;
import com.lge.stark.eddard.model.MessageStatus;
import com.lge.stark.eddard.model.Room;

public class RoomController {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RoomController.class);

	public final static RoomController SELF;

	static {
		SELF = new RoomController();
	}

	public class RoomMessage {
		public Room room;
		public Message message;

		protected RoomMessage(Room room, Message message) {
			this.room = room;
			this.message = message;
		}
	}

	public Room get(String id) throws FaultException {
		return get(ElasticsearchGateway.getClient(), id);
	}

	public Room get(Client client, String id) throws FaultException {
		GetResponse response = client.prepareGet("stark", "room", id).execute().actionGet();

		if (response.isExists() == false) { return null; }

		Room ret = Jsonizable.read(response.getSourceAsString(), Room.class);

		ret.setId(response.getId());

		return ret;
	}

	public RoomMessage create(String name, String inviterId, List<String> inviteeIds, String secretKey, String message)
			throws FaultException {
		return create(ElasticsearchGateway.getClient(), name, inviterId, inviteeIds, secretKey, message);
	}

	public RoomMessage create(Client client, String name, String inviterId, List<String> inviteeIds, String secretKey,
			String message) throws FaultException {
		List<String> users = Lists.newArrayList(inviteeIds);
		users.add(inviterId);

		Room room = new Room();

		room.setId(IdGenerator.newId());
		room.setName(name);
		room.setSecretKey(secretKey);
		room.setCreateDate(new Date());
		room.setUsers(users);

		IndexResponse response = client.prepareIndex("stark", "room").setSource(room.toJsonStringWithout("id"))
				.execute().actionGet();

		if (response.isCreated() == false) { throw new FaultException(Fault.COMMON_000); }

		room.setId(response.getId());

		Message msg = MessageController.SELF.create(room.getId(), message, inviterId);

		for (String inviteeId : inviteeIds) {
			List<String> deviceIds = ProfileServer.SELF.getDeviceIds(inviteeId);

			List<Device> devices = DeviceController.SELF.get(deviceIds);

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

		return new RoomMessage(room, msg);
	}

	public Room addUsers(String roomId, List<String> userIds) throws FaultException {
		return addUsers(ElasticsearchGateway.getClient(), roomId, userIds);
	}

	public Room addUsers(Client client, String roomId, List<String> userIds) throws FaultException {
		Room room = get(client, roomId);
		room.getUsers().addAll(userIds);

		try {
			client.update(new UpdateRequest("stark", "room", roomId)
					.doc(XContentFactory.jsonBuilder().startObject().field("users", room.getUsers()).endObject()))
					.get();
		}
		catch (InterruptedException | ExecutionException | IOException e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		return room;
	}
}