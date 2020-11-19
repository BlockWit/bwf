package com.blockwit.bwf.service;

import com.blockwit.bwf.exception.SendVerificationTokenException;

public interface EmailService {

    void sendVerificationToken(String to, String login, String code) throws SendVerificationTokenException;

    void sendPasswordRecoveryToken(String to, String login, String code) throws SendVerificationTokenException;

}
