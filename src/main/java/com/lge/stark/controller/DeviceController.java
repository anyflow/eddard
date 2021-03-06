package com.lge.stark.controller;

import java.util.Date;
import java.util.List;

import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.google.common.collect.Lists;
import com.lge.stark.FaultException;
import com.lge.stark.Jsonizable;
import com.lge.stark.gateway.ElasticsearchGateway;
import com.lge.stark.mockserver.ProfileServer;
import com.lge.stark.model.Device;
import com.lge.stark.model.Fault;
import com.lge.stark.model.PushType;

public class DeviceController {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceController.class);

	public final static DeviceController SELF;

	static {
		SELF = new DeviceController();
	}

	public Device getActive(String userId) throws FaultException {
		List<String> deviceIds = ProfileServer.SELF.getDeviceIds(userId);

		List<Device> devices = DeviceController.SELF.get(deviceIds.toArray(new String[0]));

		if (devices == null || devices.size() <= 0) { return null; }

		return devices.stream().filter(item -> {
			return item.isActive();
		}).findFirst().orElse(null);
	}

	public List<Device> get(String... deviceIds) throws FaultException {
		Client client = ElasticsearchGateway.getClient();

		MultiGetRequestBuilder builder = client.prepareMultiGet();
		builder.add("stark", "device", deviceIds);

		List<Device> ret = Lists.newArrayList();
		for (MultiGetItemResponse res : builder.execute().actionGet()) {
			if (res.getResponse().isExists() == false) {
				continue;
			}

			Device item = Jsonizable.read(res.getResponse().getSourceAsString(), Device.class);
			item.setId(res.getResponse().getId());

			ret.add(item);
		}

		return ret;
	}

	public void create(String deviceId, String receiverId, PushType type, boolean isActive) throws FaultException {
		Device device = new Device();

		device.setId(deviceId);
		device.setReceiverId(receiverId);
		device.setType(type);
		device.isActive(isActive);
		device.setCreateDate(new Date());

		Client client = ElasticsearchGateway.getClient();

		try {
			client.prepareIndex("stark", "device", device.getId()).setSource(device.toJsonStringWithout("id", "userId"))
					.execute().actionGet();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}
	}

	public void updateStatus(String deviceId, boolean isActive) throws FaultException {
		Client client = ElasticsearchGateway.getClient();

		try {
			client.update(new UpdateRequest("stark", "device", deviceId)
					.doc(XContentFactory.jsonBuilder().startObject().field("active", isActive).endObject())).get();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}
	}

	public void delete(String deviceId) throws FaultException {
		Client client = ElasticsearchGateway.getClient();

		try {
			client.prepareDelete("stark", "device", deviceId).get();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}
	}
}