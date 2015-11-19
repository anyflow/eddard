package com.lge.stark.eddard.mybatis;

import com.lge.stark.eddard.model.User;

public interface UserMapper {
	User selectByPrimaryKey(String id);

	int deleteByPrimaryKey(String id);

	int insertSelective(User user);

	int updateByPrimaryKeySelective(User user);
}