package com.blockwit.bwf.services;

import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendVerificationToken(String to, String login, String code) {
        System.out.println("To " + to + " with login " + login + " code sent " + code);
    }


}
