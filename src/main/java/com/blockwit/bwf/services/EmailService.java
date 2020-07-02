package com.blockwit.bwf.services;

public interface EmailService {

    void sendVerificationToken(String to, String login, String code);

}
