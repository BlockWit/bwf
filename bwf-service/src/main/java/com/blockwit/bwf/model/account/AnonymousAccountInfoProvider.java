package com.blockwit.bwf.model.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Set;

@Getter
@AllArgsConstructor
public class AnonymousAccountInfoProvider implements IAccountInfoProvider {

	public static final String ANONYMOUS = "ANONYMOUS";

	@Override
	public Long getId() {
		throw new UnsupportedOperationException("Anonymous account have no Id");
	}

	@Override
	public AccountType getAccountType() {
		return AccountType.ANONYMOUS;
	}

	@Override
	public String getUsername() {
		return ANONYMOUS;
	}

	@Override
	public String getEmail() {
		throw new UnsupportedOperationException("Anonymous account have no email");
	}

	@Override
	public Set<String> getAllPermissions() {
		return Collections.emptySet();
	}

	@Override
	public Set<String> getPermissions() {
		return Collections.emptySet();
	}

	@Override
	public Set<String> getRoles() {
		return Collections.emptySet();
	}

}
