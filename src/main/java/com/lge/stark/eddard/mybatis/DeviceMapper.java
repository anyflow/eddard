package com.lge.stark.eddard.mybatis;

import com.lge.stark.eddard.model.Device;

public interface DeviceMapper {
	int insert(Device device);

	int updateSelective(Device device);

	int delete(String id);
}