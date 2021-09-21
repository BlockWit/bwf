package com.blockwit.bwf.model.rest.mappers;

import com.blockwit.bwf.model.account.Account;
import com.blockwit.bwf.model.rest.AccountDTO;

import java.io.Serializable;

public class AccountDTOMapper implements Serializable {

	public static AccountDTO map(Account model) {
		return new AccountDTO(model.getId(),
			model.getLogin(),
			model.getEmail());
	}

}