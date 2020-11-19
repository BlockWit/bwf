package com.blockwit.bwf.exception;

public class NotFoundAccountServiceException extends AccountServiceException {

	String login;

	public NotFoundAccountServiceException(String login) {
		super("Account with " + login + " not found");
		this.login = login;
	}

}
