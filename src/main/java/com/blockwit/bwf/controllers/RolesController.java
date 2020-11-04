package com.blockwit.bwf.controllers;

import com.blockwit.bwf.models.entity.AppContext;
import com.blockwit.bwf.models.entity.Permission;
import com.blockwit.bwf.models.entity.Role;
import com.blockwit.bwf.models.repository.PermissionRepository;
import com.blockwit.bwf.models.repository.RoleRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@Controller
public class RolesController {

    private final RoleRepository roleRepository;

    public RolesController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/panel/roles")
    public ModelAndView appPanelRoles() {
        return new ModelAndView("redirect:/panel/roles/page/1");
    }

    @GetMapping("/panel/roles/page/{pageNumber}")
    public ModelAndView appPanelRolessPage(@PathVariable("pageNumber") int pageNumber) {
        ModelAndView modelAndView = new ModelAndView("panel/roles");

        Pageable pageRequest =
                PageRequest.of(pageNumber - 1, AppContext.DEFAULT_PAGE_SIZE, Sort.by("id").descending());
        Page<Role> page = roleRepository.findAll(pageRequest);
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
