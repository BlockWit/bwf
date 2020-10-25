package com.blockwit.bwf.models.service.exceptions;

public class WaitConfirmationAccountServiceException extends AccountServiceException {

    String login;

    public WaitConfirmationAccountServiceException(String login) {
        super("Account " + login + " wait confirmation");
        this.login = login;
    }

}
