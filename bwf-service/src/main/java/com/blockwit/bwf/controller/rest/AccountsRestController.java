package com.blockwit.bwf.controller.rest;

import com.blockwit.bwf.model.rest.AccountDTO;
import com.blockwit.bwf.model.rest.common.PageableDTO;
import com.blockwit.bwf.model.rest.mappers.AccountDTOMapper;
import com.blockwit.bwf.service.AccountService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api("REST API for Accounts")
@RequestMapping(RestUrls.REST_URL_API_V_1_ACCOUNTS)
public class AccountsRestController {

	@Autowired
	AccountService accountService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageableDTO<AccountDTO>> options(@RequestParam(name = "page", defaultValue = "1") int page,
														   @RequestParam(name = "pageSize", defaultValue = "4") int pageSize) {
		return PageableHelper.pageable(accountService, page, pageSize, AccountDTOMapper::map);
	}

}