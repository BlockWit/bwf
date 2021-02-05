package com.blockwit.bwf.service;

import org.apache.tomcat.util.buf.HexUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordServiceImpl implements PasswordService {

	final RandomService randomService;

	public PasswordServiceImpl(RandomService randomService) {
		this.randomService = randomService;
	}

	@Override
	public String generateRegistrationToken(String login) {

		String hashString = BCrypt.hashpw(randomService.nextString5() + login + System.currentTimeMillis(), BCrypt.gensalt())
			.replaceAll("\\.", "s")
			.replaceAll("\\\\", "d")
			.replaceAll("\\$", "g");

		String hexString = HexUtils.toHexString(hashString.getBytes()).substring(0, 99);
		return hexString;
	}


}
