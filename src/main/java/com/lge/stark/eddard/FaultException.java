package com.lge.stark.eddard;

public class FaultException extends Exception {

	private static final long serialVersionUID = -4063767612363345893L;

	private Fault fault;

	public FaultException(Fault fault) {
		this.fault = fault;
	}

	public Fault fault() {
		return fault;
	}

	@Override
	public String getMessage() {
		return fault.getMessage() + "\r\n\r\n" + super.getMessage();
	}
}