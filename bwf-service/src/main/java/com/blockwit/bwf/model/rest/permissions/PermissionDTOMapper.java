package com.blockwit.bwf.model.rest.permissions;

import com.blockwit.bwf.model.Permission;
import com.blockwit.bwf.model.rest.permissions.PermissionDTO;

public class PermissionDTOMapper {

	public static PermissionDTO map(Permission model) {
		return new PermissionDTO(model.getId(),
			model.getName());
	}

}
