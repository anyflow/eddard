package com.lge.stark.smp.smpframe;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public final class GenericSmpframeSerializer extends JsonSerializer<GenericSmpframe<?>> {

	@Override
	public void serialize(GenericSmpframe<?> value, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		generator.writeStartObject();

		ModelResponse mr = value.model().getClass().getAnnotation(ModelResponse.class);

		generator.writeObjectField("opcode", mr.opcode());
		generator.writeStringField("sessionId", value.sessionId());
		generator.writeNumberField("pushframeId", value.id());
		generator.writeObjectField(mr.name(), value.model());

		generator.writeEndObject();
	}
}