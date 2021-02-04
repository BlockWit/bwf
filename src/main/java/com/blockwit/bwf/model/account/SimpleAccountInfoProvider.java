package com.blockwit.bwf.model.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
@Builder
public class SimpleAccountInfoProvider implements ISourceBasedAccountInfoProvider<Account> {

	private Long id;

	private AccountType accountType;

	private Account accountInfoSource;

	private String username;

	private String email;

	private Set<String> allPermissions;

	private Set<String> roles;

}


