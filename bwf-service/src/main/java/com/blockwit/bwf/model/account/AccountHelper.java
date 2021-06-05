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

import com.blockwit.bwf.model.User;
import org.springframework.security.core.Authentication;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountHelper {

	public static final AnonymousAccountInfoProvider anonymousAccountInfoProvider = new AnonymousAccountInfoProvider();

	public static final IAccountInfoProvider getAccountInfoProvider(Authentication authentication) {
		if (authentication == null)
			return anonymousAccountInfoProvider;

		Object principal = authentication.getPrincipal();
		if (principal instanceof User) {
			Account account = ((User) principal).getAccount();
			return getAccountInfoProvider(account);
		}
		return anonymousAccountInfoProvider;
	}

	public static final IAccountInfoProvider getAccountInfoProvider(Account account) {
		Set<String> permissions = account.getPermissions().stream().map(t -> t.getName()).collect(Collectors.toSet());

		Set<String> rolesPermissions = account.getRoles().stream()
			.map(t -> t.getPermissions().stream()
				.map(l -> l.getName()).collect(Collectors.toSet()))
			.flatMap(Set::stream).collect(Collectors.toSet());

		Set<String> allPermissions = new HashSet<>();
		allPermissions.addAll(permissions);
		allPermissions.addAll(rolesPermissions);

		return SimpleAccountInfoProvider.builder()
			.id(account.getId())
			.source(account)
			.username(account.getLogin())
			.permissions(permissions)
			.roles(account.getRoles().stream().map(t -> t.getName()).collect(Collectors.toSet()))
			.allPermissions(allPermissions)
			.email(account.getEmail())
			.build();
	}

}
