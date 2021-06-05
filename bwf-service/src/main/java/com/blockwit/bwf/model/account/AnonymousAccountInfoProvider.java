/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

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
