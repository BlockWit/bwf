package com.blockwit.bwf.exception;

public class EmailBusyAccountServiceException extends AccountServiceException {

	String email;

	public EmailBusyAccountServiceException(String email) {
		super("Email " + email + " already exists");
		this.email = email;
	}

}
