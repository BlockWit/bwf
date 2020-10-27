package com.blockwit.bwf.services;

import com.blockwit.bwf.services.excetptions.SendVerificationTokenException;

public interface EmailService {

    void sendVerificationToken(String to, String login, String code) throws SendVerificationTokenException;

    void sendPasswordRecoveryToken(String to, String login, String code) throws SendVerificationTokenException;

}
