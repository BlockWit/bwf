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

package com.blockwit.bwf.service;

import com.blockwit.bwf.model.Role;
import com.blockwit.bwf.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RoleService {

	public final static String ROLE_ADMIN = "ADMIN";
	public final static String ROLE_USER = "USER";

	private final RoleRepository roleRepository;
	private final PermissionService permissionService;

	public RoleService(RoleRepository roleRepository, PermissionService permissionService) {
		this.roleRepository = roleRepository;
		this.permissionService = permissionService;
	}

	private static Role getOrCreateRole(
		RoleRepository roleRepository,
		DefaultPermissionsProvider defaultPermissionsProvider,
		String name
	) {
		return roleRepository.findByName(name).orElseGet(() -> {
			Role role = new Role();
			role.setName(name);
			role.setPermissions(defaultPermissionsProvider.getPermissions());
			return roleRepository.save(role);
		});
	}

	public Role getDefaultAdminRole() {
		return getOrCreateRole(roleRepository, () -> permissionService.getDefaultAdminPermissions(), ROLE_ADMIN);
	}

	public Role getDefaultUserRole() {
		return getOrCreateRole(roleRepository, () -> permissionService.getDefaultUserPermissions(), ROLE_USER);
	}

}
