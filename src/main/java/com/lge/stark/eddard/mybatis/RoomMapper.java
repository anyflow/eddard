package com.lge.stark.eddard.mybatis;

import com.lge.stark.eddard.model.Room;

public interface RoomMapper {
	Room selectByPrimaryKey(String id);

	int deleteByPrimaryKey(String id);

	int insertSelective(Room room);

	int updateByPrimaryKeySelective(Room room);
}