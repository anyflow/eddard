package com.lge.stark.mockserver;

import org.elasticsearch.action.get.GetResponse;

import com.lge.stark.FaultException;
import com.lge.stark.Jsonizable;
import com.lge.stark.gateway.ElasticsearchGateway;
import com.lge.stark.model.Fault;
import com.lge.stark.model.User;

public class UserServer {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserServer.class);

	public final static UserServer SELF;

	static {
		SELF = new UserServer();
	}

	public User get(String userId) throws FaultException {
		GetResponse response;
		try {
			response = ElasticsearchGateway.getClient().prepareGet("user", "user", userId).get();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FaultException(Fault.COMMON_000);
		}
		if (response.isExists() == false) { return null; }

		User ret = Jsonizable.read(response.getSourceAsString(), User.class);

		ret.setId(response.getId());

		return ret;
	}
}