package com.lge.stark.eddard.mockserver;

import org.elasticsearch.action.get.GetResponse;

import com.lge.stark.eddard.FaultException;
import com.lge.stark.eddard.Jsonizable;
import com.lge.stark.eddard.gateway.ElasticsearchGateway;
import com.lge.stark.eddard.model.User;

public class UserServer {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserServer.class);

	public final static UserServer SELF;

	static {
		SELF = new UserServer();
	}

	public User get(String userId) throws FaultException {
		GetResponse gr = ElasticsearchGateway.getClient().prepareGet("user", "user", userId).get();

		if (gr.isExists() == false) { return null; }

		User ret = Jsonizable.read(gr.getSourceAsString(), User.class);

		ret.setId(gr.getId());

		return ret;
	}
}