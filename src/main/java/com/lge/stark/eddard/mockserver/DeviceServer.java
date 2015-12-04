package com.lge.stark.eddard.mockserver;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lge.stark.eddard.model.Device;

public class DeviceServer implements MockServer {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceServer.class);

	public final static DeviceServer SELF;

	static {
		SELF = new DeviceServer();
	}

	private List<Device> store;

	public boolean isValid(String deviceId) {
		return store.stream().anyMatch(x -> {
			return x.getId().equals(deviceId);
		});
	}

	@Override
	public void load(String dataFilePath) throws IOException {

		File file = new File(dataFilePath);

		store = (new ObjectMapper()).readValue(file, new TypeReference<List<Device>>() {
		});
	}

	@Override
	public void save(String dataFilePath) throws JsonGenerationException, JsonMappingException, IOException {
		(new ObjectMapper()).writer().writeValue(new File(dataFilePath), store);
	}
}