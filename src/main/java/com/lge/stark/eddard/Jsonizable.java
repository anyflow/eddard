package com.lge.stark.eddard;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Convertable to JSON format Object(String, byte array)
 * 
 * @author hyunjeong.park@lge.com
 */
public class Jsonizable {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Jsonizable.class);
	protected static final String RUNTIME_EXCEPTION_MESSAGE = "Exception should not be occurred. See logs.";

	public static <T> T read(String json, Class<T> returnClass) {
		return read(json, returnClass, new ObjectMapper());
	}

	public static <T> T read(byte[] json, Class<T> returnClass) {
		return read(json, returnClass, new ObjectMapper());
	}

	public static <T> T read(String json, Class<T> returnClass, ObjectMapper mapper) {

		try {
			return mapper.reader().withType(returnClass).readValue(json);
		}
		catch(IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(RUNTIME_EXCEPTION_MESSAGE, e);
		}
	}

	public static <T> T read(byte[] json, Class<T> returnClass, ObjectMapper mapper) {

		try {
			return mapper.reader().withType(returnClass).readValue(json);
		}
		catch(IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(RUNTIME_EXCEPTION_MESSAGE, e);
		}
	}

	public String toJsonString() {
		return toJsonString(new ObjectMapper());
	}

	public byte[] toJsonByteArray() {
		return toJsonByteArray(new ObjectMapper());
	}

	public String toJsonString(ObjectMapper mapper) {
		try {
			return mapper.writer().writeValueAsString(this);
		}
		catch(JsonProcessingException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(RUNTIME_EXCEPTION_MESSAGE, e);
		}
	}

	public byte[] toJsonByteArray(ObjectMapper mapper) {
		try {
			return mapper.writer().writeValueAsBytes(this);
		}
		catch(JsonProcessingException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(RUNTIME_EXCEPTION_MESSAGE, e);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "|" + toJsonString();
	}
}