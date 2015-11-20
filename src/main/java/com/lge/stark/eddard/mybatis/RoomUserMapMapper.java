package com.lge.stark.eddard.mybatis;

import com.lge.stark.eddard.model.RoomUserMap;

public interface RoomUserMapMapper {
	RoomUserMap select(RoomUserMap map);

	int delete(RoomUserMap map);

	int insert(RoomUserMap map);
}