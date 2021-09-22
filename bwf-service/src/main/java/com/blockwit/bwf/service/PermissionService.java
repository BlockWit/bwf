/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.service;

import com.blockwit.bwf.model.IPageableService;
import com.blockwit.bwf.model.Permission;
import com.blockwit.bwf.model.account.Account;
import com.blockwit.bwf.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class PermissionService implements IPageableService<Permission> {

	public final static String PERMISSION_ADMIN = "ADMIN";
	public final static String PERMISSION_USER = "USER";

	private final PermissionRepository permissionRepository;

	public PermissionService(PermissionRepository permissionRepository) {
		this.permissionRepository = permissionRepository;
	}

	private static Permission getOrCreatePermission(PermissionRepository permissionRepository, String name) {
		return permissionRepository.findByName(name).orElseGet(() -> {
			Permission permission = new Permission();
			permission.setName(name);
			return permissionRepository.save(permission);
		});
	}

	public Set<Permission> getDefaultAdminPermissions() {
		return new HashSet<>(List.of(getOrCreatePermission(permissionRepository, PERMISSION_ADMIN)));
	}

	public Set<Permission> getDefaultUserPermissions() {
		return new HashSet<>(List.of(getOrCreatePermission(permissionRepository, PERMISSION_USER)));
	}

	@Override
	public Page<Permission> findPageable(Pageable pageable) {
		return permissionRepository.findAll(pageable);
	}

}
