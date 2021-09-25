package com.blockwit.bwf.model.rest.roles;

import com.blockwit.bwf.model.Role;
import com.blockwit.bwf.model.rest.roles.RoleDTO;

public class RoleDTOMapper {

	public static RoleDTO map(Role model) {
		return new RoleDTO(model.getId(),
			model.getName());
	}

}
