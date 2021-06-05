package com.blockwit.bwf.controller;

import com.blockwit.bwf.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/panel/permissions")
public class PermissionController {

	private final PermissionRepository permissionRepository;

	public PermissionController(PermissionRepository permissionRepository) {
		this.permissionRepository = permissionRepository;
	}

	@GetMapping
	public ModelAndView appPanelPermissions() {
		return new ModelAndView("redirect:/panel/permissions/page/1");
	}

	@GetMapping("/page/{pageNumber}")
	public ModelAndView appPanelPermissionsPage(@PathVariable("pageNumber") int pageNumber) {
		return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/permissions"), pageNumber, permissionRepository);
	}

}
