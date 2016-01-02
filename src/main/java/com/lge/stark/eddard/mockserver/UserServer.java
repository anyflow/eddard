package com.lge.stark.eddard.mockserver;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lge.stark.eddard.model.User;

public class UserServer implements MockServer {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserServer.class);

	public final static UserServer SELF;

	static {
		SELF = new UserServer();
	}

	private List<User> store;

	@Override
	public void load(String dataFilePath) throws IOException {

		File file = new File(dataFilePath);

		store = (new ObjectMapper()).readValue(file, new TypeReference<List<User>>() {
		});
	}

	@Override
	public void save(String dataFilePath) throws JsonGenerationException, JsonMappingException, IOException {
		(new ObjectMapper()).writer().writeValue(new File(dataFilePath), store);
	}

	public User get(String userId) {
		return store.stream().filter(x -> {
			return x.getId().equals(userId);
		}).findFirst().get();
	}
}