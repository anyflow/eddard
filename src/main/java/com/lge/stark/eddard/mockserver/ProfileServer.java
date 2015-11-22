package com.lge.stark.eddard.mockserver;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.lge.stark.eddard.Jsonizable;

public class ProfileServer {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProfileServer.class);

	private static ProfileServer instance;

	public static ProfileServer instance() {
		if (instance == null) {
			instance = new ProfileServer();
		}

		return instance;
	}

	public class Profile {
		private String userId;
		private String deviceId;

		public Profile(String userId, String deviceId) {
			this.userId = userId;
			this.deviceId = deviceId;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getDeviceId() {
			return deviceId;
		}

		public void setDeviceId(String deviceId) {
			this.deviceId = deviceId;
		}
	}

	private List<Profile> db = Lists.newArrayList();

	public List<String> getDevices(String userId) {
		List<String> ret = Lists.newArrayList();

		for (Profile item : db) {
			if (item.getUserId().equals(userId)) {
				ret.add(item.getDeviceId());
			}
		}

		return ret;
	}

	public List<String> getUsers(String deviceId) {
		List<String> ret = Lists.newArrayList();

		for (Profile item : db) {
			if (item.getDeviceId().equals(deviceId)) {
				ret.add(item.getUserId());
			}
		}

		return ret;
	}

	@SuppressWarnings("unchecked")
	public void load(String dataFilePath) throws IOException {

		File file = new File(dataFilePath);

		db = Jsonizable.read(Files.toString(file, Charsets.UTF_8), db.getClass());
	}

	public void save(String dataFilePath) throws JsonGenerationException, JsonMappingException, IOException {
		(new ObjectMapper()).writer().writeValue(new File(dataFilePath), db);
	}
}