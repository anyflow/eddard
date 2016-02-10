package com.lge.stark.eddard.smp.message;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public final class OpCodeSerializer extends JsonSerializer<OpCode> {

    @Override
    public void serialize(OpCode value, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {

        generator.writeNumber(value.id());
    }
}