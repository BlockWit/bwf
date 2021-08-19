package com.blockwit.bwf.controller.rest;

import com.blockwit.bwf.config.RESTSecurityConfig;
import com.blockwit.bwf.model.account.AccountHelper;
import com.blockwit.bwf.model.rest.AccountDTO;
import com.blockwit.bwf.model.rest.OptionDTO;
import com.blockwit.bwf.service.AccountService;
import com.blockwit.bwf.service.OptionService;
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
@Api("REST App")
@RequestMapping(RESTSecurityConfig.REST_URL_API_V_1)
public class AppRestController {

	@Autowired
	AccountService accountService;

	@Autowired
	OptionService optionService;

	@GetMapping(path = "/auth/info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AccountDTO> auth() {
		return AccountHelper.withAuthAccount(account ->
			ResponseEntity.ok(new AccountDTO(account.getId(), account.getLogin(), account.getEmail()))
		);
	}

	@GetMapping(path = "/options", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OptionDTO>> options() {
		return ResponseEntity.ok(optionService.findAll().stream()
			.map(t -> new OptionDTO(t.getId(), t.getName(), t.getType(), t.getValue(), t.getDescr()))
			.collect(Collectors.toList()));
	}

	@GetMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AccountDTO>> accounts() {
		return ResponseEntity.ok(accountService.findAll().stream()
			.map(t -> new AccountDTO(t.getId(), t.getLogin(), t.getEmail()))
			.collect(Collectors.toList()));
	}

}