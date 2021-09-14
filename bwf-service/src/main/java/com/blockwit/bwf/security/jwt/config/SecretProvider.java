package com.blockwit.bwf.security.jwt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecretProvider {

	@Value("${secret.key.expiration.time.minutes}")
	private int secretExpirationTimeInMinutes;

	@Value("${secret.key.value}")
	private String secretKey;

	public String getSecret() {
		return secretKey;
	}

	public int expirationTimeInMinutes() {
		return secretExpirationTimeInMinutes;
	}

}
