package com.lge.stark.eddard.mockserver;

import java.util.Map;

import com.google.common.collect.Maps;

public class ProfileServer {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProfileServer.class);

	private static ProfileServer instance;

	private Map<String, String> db;

	public static ProfileServer instance() {
		if (instance == null) {
			instance = new ProfileServer();
		}

		return instance;
	}

	private ProfileServer() {
		db = Maps.newHashMap();
	}

	public String get(String userId) {
		return db.get(userId);
	}

	public void set(String userId, String deviceId) {
		if (db.get(userId) != null) {
			db.remove(userId);
		}

		db.put(userId, deviceId);
	}
}