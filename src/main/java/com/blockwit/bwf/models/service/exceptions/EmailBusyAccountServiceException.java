package com.blockwit.bwf.models.service.exceptions;

public class EmailBusyAccountServiceException extends AccountServiceException {

    String email;

    public EmailBusyAccountServiceException(String email) {
        super("Email " + email + " already exists");
        this.email = email;
    }

}
