package com.lge.stark.eddard.mybatis;

import java.util.List;

import com.lge.stark.eddard.model.RoomUserMap;

public interface RoomUserMapMapper {
	List<RoomUserMap> selectByRoomId(String roomId);

	List<RoomUserMap> selectByUserId(String userId);

	RoomUserMap selectByPrimaryKey(String roomId, String userId);

	int delete(String roomId, String userId);

	int insert(String roomId, String userId);
}