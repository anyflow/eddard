package com.lge.stark.eddard.smp.message;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Ints;
import com.jayway.jsonpath.JsonPath;

public abstract class Smpframe extends com.lge.stark.eddard.Jsonizable {

	private OpCode opcode;
	private String sessionId;
	private Integer id;

	public Smpframe() {
	}

	public Smpframe(OpCode opcode, String sessionId, Integer pushframeId) {
		this.opcode = opcode;
		this.sessionId = sessionId;
		this.id = pushframeId;
	}

	@JsonProperty("opcode")
	public OpCode opcode() {
		return opcode;
	}

	public void opcode(OpCode opcode) {
		this.opcode = opcode;
	}

	@JsonProperty("sessionId")
	public String sessionId() {
		return sessionId;
	}

	public void sessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@JsonProperty("pushframeId")
	public Integer id() {
		return id;
	}

	public void id(int id) {
		this.id = id;
	}

	@JsonIgnore(value = true)
	public Integer responseSmpframeId() {
		if (id < 0) {
			return -1;
		}
		else {
			return id + 1;
		}
	}

	@JsonIgnore(value = true)
	public boolean isReturnType() {
		return opcode.isReturnType();
	}

	@JsonIgnore(value = true)
	public abstract boolean isResponseRequired();

	public static Smpframe createFrom(String json) throws JsonProcessingException, IOException {
		return createFrom(json, new ObjectMapper());
	}

	public static Smpframe createFrom(String json, ObjectMapper mapper) throws JsonProcessingException, IOException {

		Integer opcode = Ints.tryParse(JsonPath.read(json, "opcode").toString());

		return mapper.reader().forType(OpCode.getOpcodeClass(OpCode.from(opcode))).readValue(json);
	}
}