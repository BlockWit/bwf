package com.blockwit.bwf.service.rest;

import com.blockwit.bwf.controller.rest.PageableHelper;
import com.blockwit.bwf.model.rest.common.PageDTO;
import com.blockwit.bwf.model.rest.permissions.PermissionDTO;
import com.blockwit.bwf.model.rest.permissions.PermissionDTOMapper;
import com.blockwit.bwf.service.PermissionService;
import org.springframework.stereotype.Component;

@Component
public class PermissionsRestService {

	PermissionService permissionsService;

	public PermissionsRestService(PermissionService permissionsService) {
		this.permissionsService = permissionsService;
	}

	public PageDTO<PermissionDTO> findAll(int page, int pageSize) {
		return PageableHelper.pageable(
			t -> permissionsService.findPageable(t),
			page,
			pageSize,
			t -> PermissionDTOMapper.map(t));
	}

}
