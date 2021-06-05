/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.service;

import com.blockwit.bwf.exception.SendVerificationTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Component
public class EmailServiceImpl implements EmailService {

	@Value("${server.port}")
	private String serverPort;

	// TODO: Should be dynamically generate
	@Value("${verification.link.pattern}")
	private String verificationPattern;

	// TODO: Should be dynamically generate
	@Value("${recoeverypassword.link.pattern}")
	private String recoveryPasswordPattern;

	private static void sendLinkWithCode(String serverPort, String to, String login, String code, String pattern) throws SendVerificationTokenException {
		String hostname;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new SendVerificationTokenException();
		}

		String verificationLink = pattern
			.replace("%host%", hostname)
			.replace("%port%", serverPort)
			.replace("%account.login%", login)
			.replace("%account.confirmCode%", code);

		log.info("To " + to + " with login " + login + " code sent " + code);
		log.info(verificationLink);
	}

	@Override
	public void sendPasswordRecoveryToken(String to, String login, String code) throws SendVerificationTokenException {
		sendLinkWithCode(serverPort, to, login, code, recoveryPasswordPattern);
	}

	@Override
	public void sendVerificationToken(String to, String login, String code) throws SendVerificationTokenException {
		sendLinkWithCode(serverPort, to, login, code, verificationPattern);
	}

}
