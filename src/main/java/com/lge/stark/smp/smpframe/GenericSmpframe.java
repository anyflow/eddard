package com.lge.stark.smp.smpframe;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = GenericSmpframeSerializer.class)
public class GenericSmpframe<Model> extends Smpframe {

	private Model model;

	public GenericSmpframe() {
		super();
	}

	public GenericSmpframe(String sessionId, Integer smpframeId, Model model) {
		super(OpCode.CLOSE_SESSION, sessionId, smpframeId);

		this.model = model;
	}

	@Override
	public boolean isResponseRequired() {
		return false;
	}

	public Model model() {
		return model;
	}
}