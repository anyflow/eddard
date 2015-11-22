package com.lge.stark.eddard.controller;

import java.util.Date;
import java.util.List;

import com.lge.stark.eddard.Fault;
import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.IdGenerator;
import com.lge.stark.eddard.gateway.PushGateway;
import com.lge.stark.eddard.mockserver.ProfileServer;
import com.lge.stark.eddard.model.Device;
import com.lge.stark.eddard.model.Message;
import com.lge.stark.eddard.model.MessageStatus;
import com.lge.stark.eddard.model.Room;
import com.lge.stark.eddard.model.RoomUserMap;
import com.lge.stark.eddard.mybatis.MessageStatusMapper;
import com.lge.stark.eddard.mybatis.RoomMapper;
import com.lge.stark.eddard.mybatis.RoomUserMapMapper;
import com.lge.stark.eddard.mybatis.SqlConnector;
import com.lge.stark.eddard.mybatis.SqlSessionEx;

import io.netty.handler.codec.http.HttpResponseStatus;

public class RoomController {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RoomController.class);

	private static RoomController instance;

	public static RoomController instance() {
		if (instance == null) {
			instance = new RoomController();
		}

		return instance;
	}

	public Room get(String id) throws FaultException {
		SqlSessionEx session = SqlConnector.openSession(true);

		try {
			Room ret = session.getMapper(RoomMapper.class).selectByPrimaryKey(id);

			if (ret == null) { throw new FaultException(new Fault("2", "Room not found.")); }

			return ret;
		}
		finally {
			session.close();
		}
	}

	public class RoomMessage {
		public Room room;
		public Message message;

		protected RoomMessage(Room room, Message message) {
			this.room = room;
			this.message = message;
		}
	}

	public RoomMessage create(String name, String inviterId, List<String> inviteeIds, String secretKey, String message)
			throws FaultException {

		SqlSessionEx session = SqlConnector.openSession(false);

		try {
			Room room = new Room();

			room.setId(IdGenerator.newId());
			room.setName(name);
			room.setSecretKey(secretKey);
			room.setCreateDate(new Date());

			logger.debug("id : {}", room.getId());

			if (session.getMapper(RoomMapper.class)
					.insertSelective(room) <= 0) { throw new FaultException(
							new Fault("3", "Unknown DB error occured : room creation failed.",
									HttpResponseStatus.INTERNAL_SERVER_ERROR)); }

			if (session.getMapper(RoomUserMapMapper.class)
					.insert(new RoomUserMap(room.getId(), inviterId)) <= 0) { throw new FaultException(
							new Fault("3", "Unknown DB error occured : room creation failed.",
									HttpResponseStatus.INTERNAL_SERVER_ERROR)); }

			Message msg = MessageController.instance().create(session, room.getId(), message, inviterId);

			for (String inviteeId : inviteeIds) {
				List<String> deviceIds = ProfileServer.instance().getDevices(inviteeId);

				List<Device> devices = DeviceController.instance().get(deviceIds);

				if (devices == null || devices.size() <= 0) {
					continue;
				}

				Device activeDevice = devices.stream().filter(item -> {
					return item.isActive();
				}).findFirst().get();

				if (activeDevice == null) {
					continue;
				}

				MessageStatus ms = new MessageStatus();

				ms.setMessageId(msg.getId());
				ms.setDeviceId(activeDevice.getId());
				ms.setStatus(MessageStatus.Status.PEND);
				ms.setCreateDate(new Date());

				if (session.getMapper(MessageStatusMapper.class)
						.insert(ms) <= 0) { throw new FaultException(
								new Fault("3", "Unknown DB error occured : MessageStatus creation failed.",
										HttpResponseStatus.INTERNAL_SERVER_ERROR)); }

				msg.setUnreadCount(msg.getUnreadCount() + 1);

				PushGateway.instance().sendMessage(activeDevice.getType(), activeDevice.getReceiverId(), message);
			}

			session.commit();

			return new RoomMessage(room, msg);
		}
		finally {
			session.close();
		}
	}
}