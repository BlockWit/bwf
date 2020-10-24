package com.blockwit.bwf.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Can't send verification token")
public class SendVerificationTokenException extends RuntimeException {
}
