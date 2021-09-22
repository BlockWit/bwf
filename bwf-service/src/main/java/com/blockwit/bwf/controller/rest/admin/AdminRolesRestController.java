package com.blockwit.bwf.controller.rest.admin;

import com.blockwit.bwf.controller.rest.PageableHelper;
import com.blockwit.bwf.controller.rest.RestUrls;
import com.blockwit.bwf.model.rest.RoleDTO;
import com.blockwit.bwf.model.rest.common.PageDTO;
import com.blockwit.bwf.model.rest.mappers.RoleDTOMapper;
import com.blockwit.bwf.service.RoleService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api("Admin REST API for roles")
@RequestMapping(RestUrls.REST_URL_API_V_1_ADMIN_ROLES)
public class AdminRolesRestController {

	@Autowired
	RoleService roleService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageDTO<RoleDTO>> options(
		@RequestParam(name = PageableHelper.PARAM_PAGE_NUMBER, defaultValue = PageableHelper.PAGE_NUMBER_DEFAULT + "") int page,
		@RequestParam(name = PageableHelper.PARAM_PAGE_SIZE, defaultValue = PageableHelper.PAGE_SIZE_DEFAULT + "") int pageSize) {
		return PageableHelper.pageable(roleService, page, pageSize, RoleDTOMapper::map);
	}

}