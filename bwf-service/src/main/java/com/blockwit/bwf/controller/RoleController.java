/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.Role;
import com.blockwit.bwf.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/panel/roles")
public class RoleController {

	private final RoleRepository roleRepository;

	public RoleController(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@GetMapping
	public ModelAndView appPanelRoles() {
		return new ModelAndView("redirect:/panel/roles/page/1");
	}

	@GetMapping("/page/{pageNumber}")
	public ModelAndView appPanelRolessPage(@PathVariable("pageNumber") int pageNumber) {
		return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/roles"), pageNumber, roleRepository);
	}

	@GetMapping("/role/{roleId}/permissions")
	public ModelAndView rolePermissions(HttpServletRequest request,
																			RedirectAttributes redirectAttributes,
																			@PathVariable("roleId") long roleId) {
		return new OptionalExecutor<Role>().perform("Role",
			roleId,
			roleRepository,
			request,
			redirectAttributes,
			role -> new ModelAndView("panel/pages/rolePermissions", Map.of("targetRole", role)));
	}

}
