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
import com.blockwit.bwf.model.Role;
import com.blockwit.bwf.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleService implements IPageableService<Role> {

	public final static String ROLE_ADMIN = "ADMIN";
	public final static String ROLE_USER = "USER";

	private final RoleRepository roleRepository;
	private final PermissionService permissionService;

	public RoleService(RoleRepository roleRepository, PermissionService permissionService) {
		this.roleRepository = roleRepository;
		this.permissionService = permissionService;
		Set<Role> defaultValues = Set.of(
			Role.builder()
				.name(ROLE_ADMIN)
				.permissions(permissionService.getDefaultAdminPermissions())
				.build(),
			Role.builder()
				.name(ROLE_USER)
				.permissions(permissionService.getDefaultUserPermissions())
				.build()
		);
		initializeDefaultValues(defaultValues);
	}

	@Transactional
	protected void initializeDefaultValues(Set<Role> inStockPairs) {
		Map<String, Role> inRoles = inStockPairs.stream().collect(Collectors.toMap(Role::getName, t -> t));

		Set<String> defaultNames = inRoles.keySet();

		Map<String, Role> namesToDBRoles = roleRepository.findAll()
			.stream().collect(Collectors.toMap(Role::getName, t -> t));

		if (!namesToDBRoles.keySet().containsAll(defaultNames)) {
			List<Role> toSave = new ArrayList<>();
			for (String name : defaultNames) {
				if (!namesToDBRoles.containsKey(name)) {
					toSave.add(inRoles.get(name));
				}
			}

			roleRepository.saveAll(toSave);
		}
	}

	public Optional<Role> findByName(String name) {
		return roleRepository.findByName(name);
	}

	public Role getDefaultAdminRole() {
		return findByName(ROLE_ADMIN).get();
	}

	public Role getDefaultUserRole() {
		return findByName(ROLE_USER).get();
	}

	@Override
	public Page<Role> findPageable(Pageable pageable) {
		return roleRepository.findAll(pageable);
	}

	public List<Role> findAll() {
		return roleRepository.findAll();
	}

}
