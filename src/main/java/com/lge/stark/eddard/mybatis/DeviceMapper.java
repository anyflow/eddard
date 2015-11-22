package com.lge.stark.eddard.mybatis;

import java.util.List;

import com.lge.stark.eddard.model.Device;

public interface DeviceMapper {
	List<Device> selectIn(List<String> ids);

	int insert(Device device);

	int updateSelective(Device device);

	int delete(String id);
}