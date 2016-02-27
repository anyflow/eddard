package com.lge.stark.model;

import java.util.Date;
import java.util.List;

import com.lge.stark.Jsonizable;
import com.lge.stark.smp.smpframe.ModelResponse;
import com.lge.stark.smp.smpframe.OpCode;

@ModelResponse(name = "channel", opcode = OpCode.CHANNEL_CREATED)
public class Channel extends Jsonizable {

	private String id;
	private String name;
	private String secretKey;
	private List<String> userIds;
	private Date createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}