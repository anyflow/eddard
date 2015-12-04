package com.lge.stark.eddard.mockserver;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface MockServer {

	public static class MockServerException extends Exception {

		private static final long serialVersionUID = 1736421697323352635L;

		public MockServerException(String message) {
			super(message);
		}
	}

	public void load(String dataFilePath) throws IOException;

	public void save(String dataFilePath) throws JsonGenerationException, JsonMappingException, IOException;
}