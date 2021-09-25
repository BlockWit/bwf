package com.blockwit.bwf.controller.rest.admin;

import com.blockwit.bwf.controller.rest.RestUrls;
import com.blockwit.bwf.model.Permission;
import com.blockwit.bwf.model.rest.permissions.PermissionDTO;
import com.blockwit.bwf.model.rest.permissions.PermissionDTOMapper;
import com.blockwit.bwf.service.PermissionService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api("Admin REST API for permissions")
@RequestMapping(RestUrls.REST_URL_API_V_1_ADMIN_PERMISSIONS)
public class AdminPermissionsRestController extends WithListController<Permission, PermissionService, PermissionDTO> {

	public AdminPermissionsRestController(PermissionService modelService) {
		this.modelService = modelService;
		this.mapper = t -> PermissionDTOMapper.map(t);
	}

}