package com.lge.stark.eddard.model;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lge.stark.eddard.Jsonizable;

public class Device extends Jsonizable {

	public final class PushTypeSerializer extends JsonSerializer<PushType> {

		@Override
		public void serialize(PushType value, JsonGenerator generator, SerializerProvider provider)
				throws IOException, JsonProcessingException {

			generator.writeString(value.description);
		}
	}

	public final class PushTypeDeserializer extends JsonDeserializer<PushType> {

		@Override
		public PushType deserialize(final JsonParser parser, final DeserializationContext context)
				throws IOException, JsonProcessingException {

			return PushType.from(parser.getValueAsString());
		}
	}

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

	private String id;
	private String userId;
	private PushType type;
	private String receiverId;
	private boolean isActive;
	private Date createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public PushType getType() {
		return type;
	}

	public void setType(PushType type) {
		this.type = type;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void isActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}