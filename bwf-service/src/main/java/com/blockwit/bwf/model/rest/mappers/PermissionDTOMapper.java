package com.blockwit.bwf.model.rest.mappers;

import com.blockwit.bwf.model.Permission;
import com.blockwit.bwf.model.rest.PermissionDTO;

public class PermissionDTOMapper {

	public static PermissionDTO map(Permission model) {
		return new PermissionDTO(model.getId(),
			model.getName());
	}

}
