package com.blockwit.bwf.exception;

public class LoginBusyAccountServiceException extends AccountServiceException {

	String login;

	public LoginBusyAccountServiceException(String login) {
		super("Login " + login + " already exists");
		this.login = login;
	}

}
