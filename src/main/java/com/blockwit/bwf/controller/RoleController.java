package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.account.Account;
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
		return new OptionalExecutor<Account>().perform("Role",
			roleId,
			roleRepository,
			request,
			redirectAttributes,
			role -> new ModelAndView("panel/pages/rolePermissions", Map.of("targetRole", role)));
	}

}
