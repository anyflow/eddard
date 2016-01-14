package com.lge.stark.eddard.mockserver;

import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.google.common.collect.Lists;
import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.gateway.ElasticsearchGateway;
import com.lge.stark.eddard.model.Fault;

public class ProfileServer {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProfileServer.class);

	public final static ProfileServer SELF;

	static {
		SELF = new ProfileServer();
	}

	private ProfileServer() {
		super();
	}

	public List<String> getDeviceIds(String userId) throws FaultException {
		SearchResponse response;
		try {
			response = ElasticsearchGateway.getClient().prepareSearch("profile")
					.setQuery(QueryBuilders.matchQuery("userId", userId)).execute().actionGet();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		List<String> ret = Lists.newArrayList();

		if (response.getHits().getTotalHits() <= 0) { return ret; }

		for (SearchHit hit : response.getHits().hits()) {
			ret.add(hit.getSource().get("deviceId").toString());
		}

		return ret;
	}

	public List<String> getUserIds(String deviceId) throws FaultException {
		SearchResponse response;
		try {
			response = ElasticsearchGateway.getClient().prepareSearch("profile")
					.setQuery(QueryBuilders.matchQuery("deviceId", deviceId)).execute().actionGet();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}

		List<String> ret = Lists.newArrayList();

		if (response.getHits().getTotalHits() <= 0) { return ret; }

		for (SearchHit hit : response.getHits().hits()) {
			ret.add(hit.getSource().get("deviceId").toString());
		}

		return ret;
	}
}