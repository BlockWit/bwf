package com.blockwit.bwf.service;

import com.blockwit.bwf.entity.Role;
import com.blockwit.bwf.repository.RoleRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RoleService {

	public final static String ROLE_ADMIN = "ADMIN";
	public final static String ROLE_USER = "USER";

	private final RoleRepository roleRepository;
	private final PermissionsService permissionsService;

	public RoleService(RoleRepository roleRepository, PermissionsService permissionsService) {
		this.roleRepository = roleRepository;
		this.permissionsService = permissionsService;
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
		return getOrCreateRole(roleRepository, () -> permissionsService.getDefaultAdminPermissions(), ROLE_ADMIN);
	}

	public Role getDefaultUserRole() {
		return getOrCreateRole(roleRepository, () -> permissionsService.getDefaultUserPermissions(), ROLE_USER);
	}

}
