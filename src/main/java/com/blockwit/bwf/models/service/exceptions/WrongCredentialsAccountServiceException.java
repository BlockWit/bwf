package com.blockwit.bwf.models.service.exceptions;

public class WrongCredentialsAccountServiceException extends AccountServiceException {

    String login;

    public WrongCredentialsAccountServiceException(String login) {
        super("Account " + login + " wrong credentials");
        this.login = login;
    }

}
