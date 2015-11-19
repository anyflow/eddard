package com.lge.stark.eddard.business;

import java.util.Date;
import java.util.List;

import com.lge.stark.eddard.Fault;
import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.IdGenerator;
import com.lge.stark.eddard.model.Room;
import com.lge.stark.eddard.mybatis.RoomMapper;
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

	public Room create(String name, String inviterId, List<String> inviteeIds, String secretKey) throws FaultException {
		SqlSessionEx session = SqlConnector.openSession(false);

		try {

			Room ret = new Room();

			ret.setId(IdGenerator.newId());
			ret.setName(name);
			ret.setSecretKey(secretKey);
			ret.setCreateDate(new Date());

			// TODO user ID validation.

			if (session.getMapper(RoomMapper.class).insertSelective(ret) <= 0) { throw new FaultException(new Fault("3",
					"Unknown DB error occured : room creation failed.", HttpResponseStatus.INTERNAL_SERVER_ERROR)); }

			// TODO insert RoomUserMapper items

			session.commit();

			return ret;
		}
		finally {
			session.close();
		}
	}
}