package com.blockwit.bwf.security.jwt.controllers;

import com.blockwit.bwf.controller.rest.RestUrls;
import com.blockwit.bwf.security.jwt.JwtUtils;
import com.blockwit.bwf.security.jwt.config.SecretProvider;
import com.blockwit.bwf.security.jwt.model.AuthenticationRequest;
import com.blockwit.bwf.security.jwt.model.AuthenticationResponse;
import com.nimbusds.jose.JOSEException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Slf4j
@Controller
@Api(value = "Authentication controller for Managing and Browsing Topics and Queues.")
@RequestMapping(value = RestUrls.REST_URL_API_V_1)
public class RestAuthenticationController {

	private final AuthenticationManager authenticationManager;

	private final SecretProvider secretProvider;

	public RestAuthenticationController(AuthenticationManager authenticationManager,
										SecretProvider secretProvider) {
		this.authenticationManager = authenticationManager;
		this.secretProvider = secretProvider;
	}

	@RequestMapping(value = RestUrls.REST_URL_REL_AUTH,
		method = RequestMethod.POST,
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("REST API Authentication entry point")
	public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest)
		throws AuthenticationException, JOSEException, IOException {

		String login = authenticationRequest.getLogin();
		String password = authenticationRequest.getPassword();

		// throws authenticationException if it fails !
		Authentication authentication;
		try {
			authentication = this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(login, password)
			);
		} catch (BadCredentialsException e) {
			return ResponseEntity.badRequest().body("Wrong Login or password!");
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		int expirationInMinutes = secretProvider.expirationTimeInMinutes();

		String token = JwtUtils.generateHMACToken(login,
			authentication.getAuthorities(),
			secretProvider.getSecret(),
			expirationInMinutes);

		// Return the token
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}

}

