package com.lge.stark.eddard.controller;

import java.util.Date;
import java.util.List;

import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.model.Device;
import com.lge.stark.eddard.model.Fault;
import com.lge.stark.eddard.model.PushType;
import com.lge.stark.eddard.mybatis.DeviceMapper;
import com.lge.stark.eddard.mybatis.SqlConnector;
import com.lge.stark.eddard.mybatis.SqlSessionEx;

public class DeviceController {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceController.class);

	public final static DeviceController SELF;

	static {
		SELF = new DeviceController();
	}

	public List<Device> get(List<String> deviceIds) {
		SqlSessionEx session = SqlConnector.openSession(true);
		try {
			return session.getMapper(DeviceMapper.class).selectIn(deviceIds);
		}
		finally {
			session.close();
		}
	}

	public void create(String deviceId, String receiverId, PushType type, boolean isActive) throws FaultException {
		Device device = new Device();

		device.setId(deviceId);
		device.setReceiverId(receiverId);
		device.setType(type);
		device.isActive(isActive);
		device.setCreateDate(new Date());

		SqlSessionEx session = SqlConnector.openSession(false);

		try {
			if (session.getMapper(DeviceMapper.class)
					.insert(device) <= 0) { throw new FaultException(Fault.COMMON_001); }
			session.commit();
		}
		catch (org.apache.ibatis.exceptions.PersistenceException e) {
			if (e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException == false) { throw e; }

			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.DEVICE_001.replaceWith(deviceId));
		}
		finally {
			session.close();
		}
	}

	public void updateStatus(String deviceId, boolean isActive) throws FaultException {
		Device device = new Device();

		device.setId(deviceId);
		device.isActive(isActive);

		SqlSessionEx session = SqlConnector.openSession(false);

		try {
			if (session.getMapper(DeviceMapper.class)
					.updateSelective(device) <= 0) { throw new FaultException(Fault.COMMON_001); }
			session.commit();
		}
		finally {
			session.close();
		}
	}

	public void delete(String deviceId) throws FaultException {
		SqlSessionEx session = SqlConnector.openSession(false);

		try {
			if (session.getMapper(DeviceMapper.class)
					.delete(deviceId) <= 0) { throw new FaultException(Fault.COMMON_001); }
			session.commit();
		}
		finally {
			session.close();
		}
	}
}