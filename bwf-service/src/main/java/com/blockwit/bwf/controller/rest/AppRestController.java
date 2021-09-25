package com.blockwit.bwf.controller.rest;

import com.blockwit.bwf.model.account.AccountHelper;
import com.blockwit.bwf.model.rest.accounts.AccountDTO;
import com.blockwit.bwf.model.rest.accounts.AccountDTOMapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api("REST App")
@RequestMapping(RestUrls.REST_URL_API_V_1)
public class AppRestController {

	@GetMapping(path = RestUrls.REST_URL_REL_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AccountDTO> profile() {
		return AccountHelper.withAuthAccount(account -> ResponseEntity.ok(AccountDTOMapper.map(account)));
	}

}