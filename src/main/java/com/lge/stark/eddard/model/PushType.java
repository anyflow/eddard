package com.lge.stark.eddard.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = PushTypeSerializer.class)
@JsonDeserialize(using = PushTypeDeserializer.class)
public enum PushType {

	LGPS("LGPS"), GCM("GCM"), APNS("APNS");

	private final String description;

	PushType(String description) {
		this.description = description;
	}

	public String description() {
		return description;
	}

	@Override
	public String toString() {
		return description;
	}

	public static PushType from(String description) {
		if (description == null) { return null; }

		for (PushType item : values()) {
			if (item.description().equalsIgnoreCase(description.trim())) { return item; }
		}

		return null;
	}
}