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

package com.blockwit.bwf.model;

import com.blockwit.bwf.model.account.Account;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class User implements UserDetails {

	@Getter
	private final Account account;

	public User(@NotNull Account account) {
		this.account = account;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		Set<String> accountPermissions = account.getPermissions().stream().map(Permission::getName).collect(Collectors.toSet());
		account.getRoles().stream().map(Role::getPermissions).flatMap(Collection::stream).map(Permission::getName).forEach(accountPermissions::add);
		return AuthorityUtils.createAuthorityList(accountPermissions.toArray(String[]::new));
	}

	@Override
	public String getPassword() {
		return this.account.getHash();
	}

	@Override
	public String getUsername() {
		return this.account.getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.account.isApproved();
	}
}
