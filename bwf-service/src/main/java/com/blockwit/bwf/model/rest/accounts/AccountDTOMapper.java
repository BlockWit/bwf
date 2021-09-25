package com.blockwit.bwf.model.rest.accounts;

import com.blockwit.bwf.model.account.Account;
import com.blockwit.bwf.model.rest.accounts.AccountDTO;

public class AccountDTOMapper {

	public static AccountDTO map(Account model) {
		return new AccountDTO(model.getId(),
			model.getLogin(),
			model.getEmail());
	}

}