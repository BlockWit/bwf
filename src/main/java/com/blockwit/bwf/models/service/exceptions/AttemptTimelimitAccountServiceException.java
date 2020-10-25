package com.blockwit.bwf.models.service.exceptions;

public class AttemptTimelimitAccountServiceException extends AccountServiceException {

    private String login;

    private long limit;

    public AttemptTimelimitAccountServiceException(String login, long limit) {
        super("You can try again after " + limit + " milliseconds");
        this.limit = limit;
        this.login = login;
    }

    public long getLimit() {
        return limit;
    }

}
