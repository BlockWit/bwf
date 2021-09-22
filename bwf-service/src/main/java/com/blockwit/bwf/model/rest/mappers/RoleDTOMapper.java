package com.blockwit.bwf.model.rest.mappers;

import com.blockwit.bwf.model.Role;
import com.blockwit.bwf.model.rest.RoleDTO;

public class RoleDTOMapper {

	public static RoleDTO map(Role model) {
		return new RoleDTO(model.getId(),
			model.getName());
	}

}
