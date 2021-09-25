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
import com.blockwit.bwf.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PermissionService implements IPageableService<Permission> {

	public final static String PERMISSION_ADMIN = "ADMIN";
	public final static String PERMISSION_USER = "USER";

	private final PermissionRepository permissionRepository;

	public PermissionService(PermissionRepository permissionRepository) {
		this.permissionRepository = permissionRepository;
		List<String> perms = List.of(PERMISSION_ADMIN, PERMISSION_USER);
		initializeDefaultValues(perms);
	}

	@Transactional
	protected void initializeDefaultValues(List<String> names) {
		Map<String, Permission> namesToModels = permissionRepository.findAll()
			.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));

		if (!namesToModels.keySet().containsAll(names)) {
			List<Permission> toSave = new ArrayList<>();
			for (String name : names) {
				if (!namesToModels.containsKey(name)) {
					toSave.add(Permission.builder().name(name).build());
				}
			}

			permissionRepository.saveAll(toSave);
		}
	}

	public Optional<Permission> getPermission(String name) {
		return permissionRepository.findByName(name);
	}

	public Set<Permission> getDefaultAdminPermissions() {
		return Set.of(getPermission(PERMISSION_ADMIN)).stream()
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toSet());
	}

	public Set<Permission> getDefaultUserPermissions() {
		return Set.of(getPermission(PERMISSION_USER)).stream()
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toSet());
	}

	@Override
	public Page<Permission> findPageable(Pageable pageable) {
		return permissionRepository.findAll(pageable);
	}

	public List<Permission> findAll() {
		return permissionRepository.findAll();
	}

	public Optional<Permission> findByName(String permissionName) {
		return permissionRepository.findByName(permissionName);
	}
}
