package com.lge.stark.eddard.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public final class PushTypeDeserializer extends JsonDeserializer<PushType> {

	@Override
	public PushType deserialize(final JsonParser parser, final DeserializationContext context)
			throws IOException, JsonProcessingException {

		return PushType.from(parser.getValueAsString());
	}
}