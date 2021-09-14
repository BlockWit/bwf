package com.blockwit.bwf.security.jwt.exceptions;

public class JwtBadSignatureException extends RuntimeException {

    public JwtBadSignatureException(String message) {
        super(message);
    }

}
