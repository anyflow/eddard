package com.lge.stark.eddard.business;

import java.util.Date;
import java.util.List;

import com.lge.stark.eddard.Fault;
import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.IdGenerator;
import com.lge.stark.eddard.gateway.DeviceGateway;
import com.lge.stark.eddard.gateway.PushGateway;
import com.lge.stark.eddard.model.Device;
import com.lge.stark.eddard.model.Message;
import com.lge.stark.eddard.model.Room;
import com.lge.stark.eddard.model.RoomUserMap;
import com.lge.stark.eddard.mybatis.RoomMapper;
import com.lge.stark.eddard.mybatis.RoomUserMapMapper;
import com.lge.stark.eddard.mybatis.SqlConnector;
import com.lge.stark.eddard.mybatis.SqlSessionEx;

import io.netty.handler.codec.http.HttpResponseStatus;

public class RoomBiz {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RoomBiz.class);

	private static RoomBiz instance;

	public static RoomBiz instance() {
		if (instance == null) {
			instance = new RoomBiz();
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
			if (session.getMapper(RoomMapper.class).selectByPrimaryKey(
					inviterId) == null) { throw new FaultException(new Fault("4", "Invalid Inviter ID")); }

			Date now = new Date();

			Room room = new Room();

			room.setId(IdGenerator.newId());
			room.setName(name);
			room.setSecretKey(secretKey);
			room.setCreateDate(now);

			if (session.getMapper(RoomMapper.class)
					.insertSelective(room) <= 0) { throw new FaultException(
							new Fault("3", "Unknown DB error occured : room creation failed.",
									HttpResponseStatus.INTERNAL_SERVER_ERROR)); }

			if (session.getMapper(RoomUserMapMapper.class)
					.insert(new RoomUserMap(room.getId(), inviterId)) <= 0) { throw new FaultException(
							new Fault("3", "Unknown DB error occured : room creation failed.",
									HttpResponseStatus.INTERNAL_SERVER_ERROR)); }

			Message msg = MessageBiz.instance().create(session, room.getId(), message, inviterId, inviteeIds.size());

			session.commit();

			for (String inviteeId : inviteeIds) {
				Device device = DeviceGateway.instance().getLogined(inviteeId);
				if (device == null) {
					continue;
				}

				PushGateway.instance().sendMessage(device.getPushType(), device.getReceiverId(), message);
			}

			return new RoomMessage(room, msg);
		}
		finally {
			session.close();
		}
	}
}