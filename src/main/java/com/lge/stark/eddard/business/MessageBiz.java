package com.lge.stark.eddard.business;

import java.util.Date;

import com.lge.stark.eddard.IdGenerator;
import com.lge.stark.eddard.model.Message;
import com.lge.stark.eddard.mybatis.MessageMapper;
import com.lge.stark.eddard.mybatis.SqlSessionEx;

public class MessageBiz {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MessageBiz.class);

	private static MessageBiz instance;

	public static MessageBiz instance() {
		if (instance == null) {
			instance = new MessageBiz();
		}

		return instance;
	}

	public Message create(SqlSessionEx session, String roomId, String message, String creatorId, int readCount) {

		Message ret = new Message();

		ret.setId(IdGenerator.newId());
		ret.setCreateDate(new Date());
		ret.setCreatorId(creatorId);
		ret.setMessage(message);
		ret.setReadCount(readCount);
		ret.setRoomId(roomId);

		session.getMapper(MessageMapper.class).insertSelective(ret);

		return ret;
	}
}
