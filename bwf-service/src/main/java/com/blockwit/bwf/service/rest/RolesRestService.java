package com.blockwit.bwf.service.rest;

import com.blockwit.bwf.controller.rest.PageableHelper;
import com.blockwit.bwf.model.rest.common.PageDTO;
import com.blockwit.bwf.model.rest.roles.RoleDTO;
import com.blockwit.bwf.model.rest.roles.RoleDTOMapper;
import com.blockwit.bwf.service.RoleService;
import org.springframework.stereotype.Component;

@Component
public class RolesRestService {

	RoleService rolesService;

	public RolesRestService(RoleService rolesService) {
		this.rolesService = rolesService;
	}

	public PageDTO<RoleDTO> findAll(int page, int pageSize) {
		return PageableHelper.pageable(
			t -> rolesService.findPageable(t),
			page,
			pageSize,
			t -> RoleDTOMapper.map(t));
	}

}
