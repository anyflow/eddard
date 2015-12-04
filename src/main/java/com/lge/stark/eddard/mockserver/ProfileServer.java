package com.lge.stark.eddard.mockserver;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.lge.stark.eddard.model.Profile;

public class ProfileServer implements MockServer {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProfileServer.class);

	public final static ProfileServer SELF;

	static {
		SELF = new ProfileServer();
	}

	protected List<Profile> store;

	private ProfileServer() {
		super();
		store = Lists.newArrayList();
	}

	@Override
	public void load(String dataFilePath) throws IOException {

		File file = new File(dataFilePath);

		store = (new ObjectMapper()).readValue(file, new TypeReference<List<Profile>>() {
		});
	}

	@Override
	public void save(String dataFilePath) throws JsonGenerationException, JsonMappingException, IOException {
		(new ObjectMapper()).writer().writeValue(new File(dataFilePath), store);
	}

	public List<String> getDevices(String userId) {
		List<String> ret = Lists.newArrayList();

		store.forEach(item -> {
			if (item.getUserId().equals(userId) == false) { return; }

			ret.add(item.getDeviceId());
		});

		return ret;
	}

	public List<String> getUsers(String deviceId) {
		List<String> ret = Lists.newArrayList();

		for (Profile item : store) {
			if (item.getDeviceId().equals(deviceId)) {
				ret.add(item.getUserId());
			}
		}

		return ret;
	}

	public Profile getLogined(String userId) {
		return store.stream().filter(x -> {
			return x.getUserId().equals(userId) && x.isLogined();
		}).findAny().orElse(null);
	}

	public void setLogin(String userId, String deviceId) {
		Iterator<Profile> itr = store.stream().filter(x -> {
			return x.getUserId().equals(userId);
		}).iterator();

		if (itr.hasNext()) {
			while (itr.hasNext()) {
				Profile item = itr.next();

				item.setLogined(item.getDeviceId().equals(deviceId));
			}
		}
		else {
			Profile item = new Profile();
			item.setUserId(userId);
			item.setDeviceId(deviceId);
			item.setLogined(true);

			updateOrAdd(item);
		}
	}

	public void setLogout(String userId) {
		Iterator<Profile> itr = store.stream().filter(x -> {
			return x.getUserId().equals(userId);
		}).iterator();

		while (itr.hasNext()) {
			Profile item = itr.next();

			item.setLogined(false);
		}
	}

	public void updateOrAdd(Profile profile) {
		Profile finded = store.stream().filter(x -> {
			return x.getUserId().equals(profile.getUserId()) && x.getDeviceId().equals(x.getDeviceId());
		}).findAny().orElse(null);

		if (finded != null) {
			finded.setLogined(profile.isLogined());
		}
		else {
			store.add(profile);
		}
	}
}