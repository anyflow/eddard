package com.lge.stark.eddard.mybatis;

import com.lge.stark.eddard.model.Message;

public interface MessageMapper {
	int insertSelective(Message message);
}