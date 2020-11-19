package com.blockwit.bwf.exception;

public class WrongCredentialsAccountServiceException extends AccountServiceException {

	String login;

	public WrongCredentialsAccountServiceException(String login) {
		super("Account " + login + " wrong credentials");
		this.login = login;
	}

}
