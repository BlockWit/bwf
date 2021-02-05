package com.blockwit.bwf.service;

import com.blockwit.bwf.model.Role;
import com.blockwit.bwf.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
