package com.lge.stark.eddard.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.google.common.collect.Lists;
import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.Jsonizable;
import com.lge.stark.eddard.gateway.ElasticsearchGateway;
import com.lge.stark.eddard.model.Device;
import com.lge.stark.eddard.model.Fault;
import com.lge.stark.eddard.model.PushType;

public class DeviceController {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceController.class);

	public final static DeviceController SELF;

	static {
		SELF = new DeviceController();
	}

	public List<Device> get(List<String> deviceIds) throws FaultException {
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

		client.prepareIndex("stark", "device", device.getId()).setSource(device.toJsonStringWithout("id", "userId"))
				.execute().actionGet();
	}

	public void updateStatus(String deviceId, boolean isActive) throws FaultException {
		Client client = ElasticsearchGateway.getClient();

		try {
			client.update(new UpdateRequest("stark", "device", deviceId)
					.doc(XContentFactory.jsonBuilder().startObject().field("active", isActive).endObject())).get();
		}
		catch (InterruptedException | ExecutionException | IOException e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}
	}

	public void delete(String deviceId) throws FaultException {
		Client client = ElasticsearchGateway.getClient();

		client.prepareDelete("stark", "device", deviceId).get();
	}

	public Device get(String deviceId) throws FaultException {
		Client client = ElasticsearchGateway.getClient();

		GetResponse response = client.prepareGet("stark", "device", deviceId).execute().actionGet();

		if (response.isExists() == false) { return null; }

		Device ret = Jsonizable.read(response.getSourceAsString(), Device.class);

		ret.setId(response.getId());

		return ret;
	}
}