package com.blockwit.bwf.controller.rest.admin;

import com.blockwit.bwf.controller.rest.RestUrls;
import com.blockwit.bwf.model.account.Account;
import com.blockwit.bwf.model.rest.accounts.AccountDTO;
import com.blockwit.bwf.model.rest.accounts.AccountDTOMapper;
import com.blockwit.bwf.service.AccountService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api("Admin REST API for accounts")
@RequestMapping(RestUrls.REST_URL_API_V_1_ADMIN_ACCOUNTS)
public class AdminAccountsRestController extends WithListController<Account, AccountService, AccountDTO> {

	public AdminAccountsRestController(AccountService modelService) {
		this.modelService = modelService;
		this.mapper = t -> AccountDTOMapper.map(t);
	}

}