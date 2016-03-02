package com.lge.stark.smp.smpframe;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Maps;

@JsonSerialize(using = OpCodeSerializer.class)
@JsonDeserialize(using = OpCodeDeserializer.class)
public enum OpCode {

	// 0 ~ 49 client to server code
	INITIALIZE(0, "Session initialization"),

	// 50 ~ 99 server to client code
	SEND_NOTIFICATION_MESSAGE(50, "Sends a notification message"), 
	SET_HEARTBEAT_RATE(51, "Set heartbeat rate"), 
	REDIRECT(52, "Redirect"),

	// 100 ~ 149 common code
	CLOSE_SESSION(100, "Inform the sender will close the session"), 
	IS_ALIVE(101, "Check whether the opponent is alive and connected to its session"),

	// 150 ~ 199 error code
	ERROR_INTERNAL_UNKNOWN(150, "Error occurred internally with unknown reason"), 
	ERROR_INVALID_SMPFRAME_FORMAT(151, "Returns invalid smpframe format error"), 
	ERROR_NO_SESSION_ALLOCATED(153, "No session has been allocated"), 
	ERROR_SESSION_EXPIRED(154, "The session({}) expired"), 
	ERROR_INVALID_DEVICEID(155, "Device ID({}) validation failed"), 
	ERROR_INVALID_NETWORKTYPE(157, "Inputed network type({}) is invalid"), 
	ERROR_STARK_SERVICE(160, "Error of Stark Service. See sub error message"),

	// 200 ~ 255 return code
	RETURN_OK(200, "Processed Successfully"),

	// 300 ~ Eddard specific
	REGISTER_DEVICE(300, "Register a device"), 
	DELETE_DEVICE(301, "Delete a device"), 
	UPDATE_DEVICE_STATUS(302, "Update status of a device"), 
	RETRIEVE_FRIENDS(303, "Get friends"),
	CREATE_CHANNEL(304, "Create a channel"),
	CREATE_MESSAGE(305, "Create a message"),
	LEAVE_CHANNEL(306, "Leave a channel"),
	MESSAGE_RECEIVED(307, "A message received"),
	GET_USERS(308, "Get users"),

	USERS_RETRIEVED(353, "Return of GET_FRIEND / GET_USERS"), 
	CHANNEL_CREATED(354, "Return of CREATE_CHANNEL"),
	MESSAGE_CREATED(355, "Return of CREATE_MESSAGE"),
	CHANNEL_LEFT(356, "A user left the channel"),
	UNREAD_COUNT_CHANGED(357, "Unread count changed"),
	
	INVALID_OPCODE(999, "The Invalid OpCode");
	
	private static final Map<OpCode, Class<? extends Smpframe>> FRAMECLASS_MAPPER;

	static {
		FRAMECLASS_MAPPER = Maps.newHashMap();

		FRAMECLASS_MAPPER.put(INITIALIZE, Initialize.class);
		FRAMECLASS_MAPPER.put(SET_HEARTBEAT_RATE, SetHeartbeatRate.class);
		FRAMECLASS_MAPPER.put(REDIRECT, Redirect.class);
		FRAMECLASS_MAPPER.put(CLOSE_SESSION, CloseSession.class);
		FRAMECLASS_MAPPER.put(IS_ALIVE, IsAlive.class);
		
		FRAMECLASS_MAPPER.put(ERROR_STARK_SERVICE, ErrorStarkService.class);

		FRAMECLASS_MAPPER.put(REGISTER_DEVICE, RegisterDevice.class);
		FRAMECLASS_MAPPER.put(DELETE_DEVICE, DeleteDevice.class);
		FRAMECLASS_MAPPER.put(UPDATE_DEVICE_STATUS, UpdateDeviceStatus.class);
		FRAMECLASS_MAPPER.put(RETRIEVE_FRIENDS, RetrieveFriends.class);
		FRAMECLASS_MAPPER.put(CREATE_CHANNEL, CreateChannel.class);
		FRAMECLASS_MAPPER.put(CREATE_MESSAGE, CreateMessage.class);
		FRAMECLASS_MAPPER.put(LEAVE_CHANNEL, LeaveChannel.class);
		FRAMECLASS_MAPPER.put(MESSAGE_RECEIVED, MessageReceived.class);
		FRAMECLASS_MAPPER.put(GET_USERS, GetUsers.class);
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