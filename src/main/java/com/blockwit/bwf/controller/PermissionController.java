package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.AppContext;
import com.blockwit.bwf.model.Permission;
import com.blockwit.bwf.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
		ModelAndView modelAndView = new ModelAndView("panel/pages/permissions");
		// TODO move prev and next page url calculation logic to views
		Pageable pageRequest =
			PageRequest.of(pageNumber - 1, AppContext.DEFAULT_PAGE_SIZE, Sort.by("id").descending());
		Page<Permission> page = permissionRepository.findAll(pageRequest);
		int totalPages = page.getTotalPages();

		if (pageNumber > 1)
			modelAndView.addObject("prevPageUrl", pageNumber - 1);
		if (pageNumber < totalPages)
			modelAndView.addObject("nextPageUrl", pageNumber + 1);

		modelAndView.addObject("pageContent", page.getContent());
		modelAndView.addObject("title", "Main page");

		return modelAndView;
	}

}
