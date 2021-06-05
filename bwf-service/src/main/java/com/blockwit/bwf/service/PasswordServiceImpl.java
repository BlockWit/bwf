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
