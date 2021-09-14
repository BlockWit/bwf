package com.blockwit.bwf.security.jwt.exceptions;

public class MalformedJwtException extends RuntimeException {

    public MalformedJwtException(String message) {
        super(message);
    }

}
