package com.lge.stark.eddard.mockserver;

import java.util.Map;

import com.google.common.collect.Maps;
import com.lge.stark.eddard.model.User;

public class UserServer {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserServer.class);

	private static UserServer instance;

	private Map<String, User> db;

	public static UserServer instance() {
		if (instance == null) {
			instance = new UserServer();
		}

		return instance;
	}

	private UserServer() {
		db = Maps.newHashMap();
	}

	public User get(String userId) {
		return db.get(userId);
	}

	public void set(User user) {
		if (db.get(user.getId()) != null) {
			db.remove(user.getId());
		}

		db.put(user.getId(), user);
	}
}