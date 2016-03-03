package com.lge.stark.controller;

import java.util.Date;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;

import com.lge.stark.FaultException;
import com.lge.stark.Jsonizable;
import com.lge.stark.gateway.ElasticsearchGateway;
import com.lge.stark.model.Fault;
import com.lge.stark.model.MessageStatus;
import com.lge.stark.model.MessageStatus.Status;

public class MessageStatusController {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MessageStatusController.class);

	public final static MessageStatusController SELF;

	static {
		SELF = new MessageStatusController();
	}

	public void create(Client client, String messageId, String deviceId, MessageStatus.Status status)
			throws FaultException {
		IndexResponse response;
		MessageStatus ms = new MessageStatus();

		Date now = new Date();

		ms.setMessageId(messageId);
		ms.setDeviceId(deviceId);
		ms.setStatus(status);
		ms.setCreateDate(now);
		if (status == Status.SENT) {
			ms.setSentDate(now);
		}

		response = client.prepareIndex("stark", "messageStatus", ms.getId()).setSource(ms.toJsonStringWithout("id"))
				.execute().actionGet();

		if (response.isCreated() == false) { throw new FaultException(Fault.COMMON_000); }
	}

	public MessageStatus get(String messageId, String deviceId) throws FaultException {
		Client client = ElasticsearchGateway.getClient();

		return get(client, messageId, deviceId);
	}

	public MessageStatus get(Client client, String messageId, String deviceId) throws FaultException {
		String id = MessageStatus.getId(messageId, deviceId);

		GetResponse response;
		try {
			response = client.prepareGet("stark", "messageStatus", id).execute().actionGet();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		if (response.isExists() == false) { return null; }

		return Jsonizable.read(response.getSourceAsString(), MessageStatus.class);
	}

	public void update(MessageStatus messageStatus) throws FaultException {
		Client client = ElasticsearchGateway.getClient();

		update(client, messageStatus);
	}

	public void update(Client client, MessageStatus messageStatus) throws FaultException {
		try {
			client.update(new UpdateRequest("stark", "messageStatus", messageStatus.getId())
					.doc(messageStatus.toJsonStringWithout("id"))).get();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}
	}
}