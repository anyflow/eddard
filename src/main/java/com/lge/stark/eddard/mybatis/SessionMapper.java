package com.lge.stark.eddard.mybatis;

import com.lge.stark.eddard.model.Session;

public interface SessionMapper {
	Session selectByPrimaryKey(String id);

	int deleteByPrimaryKey(String id);

	int insertSelective(Session session);

	int updateByPrimaryKeySelective(Session session);
}