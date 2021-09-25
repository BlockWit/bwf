package com.blockwit.bwf.controller.rest.admin;

import com.blockwit.bwf.controller.rest.RestUrls;
import com.blockwit.bwf.model.Role;
import com.blockwit.bwf.model.rest.roles.RoleDTO;
import com.blockwit.bwf.model.rest.roles.RoleDTOMapper;
import com.blockwit.bwf.service.RoleService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api("Admin REST API for roles")
@RequestMapping(RestUrls.REST_URL_API_V_1_ADMIN_ROLES)
public class AdminRolesRestController extends WithListController<Role, RoleService, RoleDTO> {

	public AdminRolesRestController(RoleService modelService) {
		this.modelService = modelService;
		this.mapper = t -> RoleDTOMapper.map(t);
	}

}