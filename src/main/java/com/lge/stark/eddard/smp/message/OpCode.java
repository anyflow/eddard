package com.lge.stark.eddard.smp.message;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Maps;

@JsonSerialize(using = OpCodeSerializer.class)
@JsonDeserialize(using = OpCodeDeserializer.class)
public enum OpCode {

	// 0 ~ 49 client to server code
	CONNECT(0, "Connect to Server"),

	// 50 ~ 99 server to client code
	SEND_NOTIFICATION_MESSAGE(50, "Sends a notification message."), 
	SET_HEARTBEAT_RATE(51, "Set heartbeat rate."), 
	REDIRECT(52, "Redirect."),

	// 100 ~ 149 common code
	CLOSE_SESSION(100, "Inform the sender will close the session."),
	IS_ALIVE(101, "Check whether the opponent is alive and connected to its session."),

	// 150 ~ 199 error code
	ERROR_INTERNAL_UNKNOWN(150, "Error occurred internally with unknown reason."), 
	ERROR_INVALID_SMPFRAME_FORMAT(151, "Returns invalid smpframe format error."), 
	ERROR_NO_SESSION_ALLOCATED(153, "No session has been allocated."), 
	ERROR_SESSION_EXPIRED(154, "The session({}) expired."),
    ERROR_INVALID_DEVICEID(155, "Device ID({}) validation failed."),
    ERROR_INVALID_NETWORKTYPE(157, "Inputed network type({}) is invalid."),
    
	// 200 ~ 255 return code
	RETURN_OK(200, "Processed Successfully.");

	private static final Map<OpCode, Class<? extends Smpframe>> FRAMECLASS_MAPPER;

	static {
		FRAMECLASS_MAPPER = Maps.newHashMap();

		FRAMECLASS_MAPPER.put(CONNECT, Connect.class);
	}

	public static OpCode from(int id) {
		for (OpCode item : values()) {
			if (item.id() == id) { return item; }
		}

		return null;
	}

	public static Class<? extends Smpframe> getOpcodeClass(OpCode code) {
		return FRAMECLASS_MAPPER.get(code);
	}

	private int id;
	private String description;

	private OpCode(int id, String description) {
		this.id = id;
		this.description = description;
	}

	public int id() {
		return id;
	}

	public String description() {
		return description;
	}

	private static final int MIN_RETURN_TYPE = 150;

	public boolean isReturnType() {
		return id >= MIN_RETURN_TYPE;
	}
}