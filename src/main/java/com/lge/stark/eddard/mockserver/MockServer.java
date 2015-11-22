package com.lge.stark.eddard.mockserver;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.lge.stark.eddard.Jsonizable;

public class MockServer<Model> {

	@SuppressWarnings("unused")
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MockServer.class);

	private Map<String, Model> db;

	protected MockServer() {
		db = Maps.newHashMap();
	}

	public Model get(String id) {
		return db.get(id);
	}

	public void set(String id, Model model) {
		if (db.get(id) != null) {
			db.remove(id);
		}

		db.put(id, model);
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