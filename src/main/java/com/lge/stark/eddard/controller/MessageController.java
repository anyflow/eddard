package com.lge.stark.eddard.controller;

import java.util.Date;

import com.lge.stark.eddard.IdGenerator;
import com.lge.stark.eddard.model.Message;
import com.lge.stark.eddard.mybatis.MessageMapper;
import com.lge.stark.eddard.mybatis.SqlSessionEx;

public class MessageController {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MessageController.class);

	public final static MessageController SELF;

	static {
		SELF = new MessageController();
	}

	public Message create(SqlSessionEx session, String roomId, String message, String creatorId) {

		Message ret = new Message();

		ret.setId(IdGenerator.newId());
		ret.setCreateDate(new Date());
		ret.setCreatorId(creatorId);
		ret.setMessage(message);
		ret.setRoomId(roomId);

		session.getMapper(MessageMapper.class).insertSelective(ret);

		return ret;
	}
}