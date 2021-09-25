package com.blockwit.bwf.service.rest;

import com.blockwit.bwf.controller.rest.PageableHelper;
import com.blockwit.bwf.model.rest.accounts.AccountDTO;
import com.blockwit.bwf.model.rest.accounts.AccountDTOMapper;
import com.blockwit.bwf.model.rest.common.PageDTO;
import com.blockwit.bwf.service.AccountService;
import org.springframework.stereotype.Component;

@Component
public class AccountsRestService {

	AccountService accountsService;

	public AccountsRestService(AccountService accountsService) {
		this.accountsService = accountsService;
	}

	public PageDTO<AccountDTO> findAll(int page, int pageSize) {
		return PageableHelper.pageable(
			t -> accountsService.findPageable(t),
			page,
			pageSize,
			t -> AccountDTOMapper.map(t));
	}

}
