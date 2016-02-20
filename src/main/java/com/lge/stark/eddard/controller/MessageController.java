package com.lge.stark.eddard.controller;

import java.util.Date;
import java.util.List;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.google.common.collect.Lists;
import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.Jsonizable;
import com.lge.stark.eddard.gateway.ElasticsearchGateway;
import com.lge.stark.eddard.model.Fault;
import com.lge.stark.eddard.model.Message;

public class MessageController {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MessageController.class);

	public final static MessageController SELF;

	static {
		SELF = new MessageController();
	}

	public Message create(String channelId, String message, String creatorId) throws FaultException {

		Message ret = new Message();

		ret.setCreateDate(new Date());
		ret.setCreatorId(creatorId);
		ret.setText(message);
		ret.setChannelId(channelId);

		Client client = ElasticsearchGateway.getClient();

		IndexResponse response;
		try {
			response = client.prepareIndex("stark", "message").setSource(ret.toJsonStringWithout("id")).execute()
					.actionGet();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		if (response.isCreated() == false) { throw new FaultException(Fault.COMMON_000); }

		ret.setId(response.getId());

		return ret;
	}

	public void setUnreadCount(String messageId, int unreadCount) throws FaultException {
		Client client = ElasticsearchGateway.getClient();

		try {
			client.update(new UpdateRequest("stark", "message", messageId)
					.doc(XContentFactory.jsonBuilder().startObject().field("unreadCount", unreadCount).endObject()))
					.get();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}
	}

	public List<Message> getMessageAll(String id) throws FaultException {
		SearchResponse response;
		try {
			response = ElasticsearchGateway.getClient().prepareSearch("stark").setTypes("message")
					.setQuery(QueryBuilders.matchQuery("channelId", id)).execute().actionGet();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		List<Message> ret = Lists.newArrayList();

		logger.debug("hits : {}", response.getHits().getTotalHits());

		if (response.getHits().getTotalHits() <= 0) { return ret; }

		for (SearchHit hit : response.getHits().hits()) {

			Message item = Jsonizable.read(hit.getSourceAsString(), Message.class);
			item.setId(hit.getId());

			logger.debug(item.toJsonString());

			ret.add(item);
		}

		return ret;
	}
}