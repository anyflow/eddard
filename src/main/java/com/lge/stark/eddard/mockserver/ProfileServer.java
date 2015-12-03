package com.lge.stark.eddard.mockserver;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.lge.stark.eddard.model.Profile;

public class ProfileServer {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProfileServer.class);

	public final static ProfileServer SELF;

	static {
		SELF = new ProfileServer();
	}

	private List<Profile> db = Lists.newArrayList();

	public List<String> getDevices(String userId) {
		List<String> ret = Lists.newArrayList();

		db.forEach(item -> {
			if (item.getUserId().equals(userId) == false) { return; }

			ret.add(item.getDeviceId());
		});

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

	public void load(String dataFilePath) throws IOException {

		File file = new File(dataFilePath);

		db = (new ObjectMapper()).readValue(file, new TypeReference<List<Profile>>() {
		});
	}

	public void save(String dataFilePath) throws JsonGenerationException, JsonMappingException, IOException {
		(new ObjectMapper()).writer().writeValue(new File(dataFilePath), db);
	}
}