package com.lge.stark.eddard.smp.smpframe;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public final class OpCodeDeserializer extends JsonDeserializer<OpCode> {

	@Override
	public OpCode deserialize(final JsonParser parser, final DeserializationContext context)
			throws IOException, JsonProcessingException {

		return OpCode.from(parser.getIntValue());
	}
}