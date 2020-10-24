package com.blockwit.bwf.services;

import com.blockwit.bwf.exceptions.SendVerificationTokenException;

public interface EmailService {

    void sendVerificationToken(String to, String login, String code) throws SendVerificationTokenException;

}
