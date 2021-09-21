package com.blockwit.bwf.controller.rest;

import com.blockwit.bwf.model.rest.AccountDTO;
import com.blockwit.bwf.model.rest.mappers.AccountDTOMapper;
import com.blockwit.bwf.security.RESTSecurityConfig;
import com.blockwit.bwf.service.AccountService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api("REST API for Accounts")
@RequestMapping(RestUrls.REST_URL_API_V_1_ACCOUNTS)
public class AccountsRestController {

	@Autowired
	AccountService accountService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AccountDTO>> accounts() {
		return ResponseEntity.ok(accountService.findAll().stream()
			.map(model -> AccountDTOMapper.map(model))
			.collect(Collectors.toList()));
	}

}