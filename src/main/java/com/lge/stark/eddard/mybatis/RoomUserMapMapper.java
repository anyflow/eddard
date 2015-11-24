package com.lge.stark.eddard.mybatis;

import java.util.List;

import com.lge.stark.eddard.model.RoomUserMap;

public interface RoomUserMapMapper {
	List<RoomUserMap> selectUsersOf(String roomId);

	List<RoomUserMap> selectRoomsOf(String userId);

	int delete(RoomUserMap map);

	int insert(RoomUserMap map);
}