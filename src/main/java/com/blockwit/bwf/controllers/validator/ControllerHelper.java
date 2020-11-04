package com.blockwit.bwf.controllers.validator;

import com.blockwit.bwf.models.entity.AppContext;
import com.blockwit.bwf.models.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

public class ControllerHelper {

//    public static ModelAndView getPage(int pageNumber, String pageName) {
//        ModelAndView modelAndView = new ModelAndView("panel/options");
//
//        Pageable pageRequest =
//                PageRequest.of(pageNumber - 1, AppContext.DEFAULT_PAGE_SIZE, Sort.by("id").descending());
//        Page<Permission> page = permissionRepository.findAll(pageRequest);
//        int totalPages = page.getTotalPages();
//
//        if (pageNumber > 1)
//            modelAndView.addObject("prevPageUrl", pageNumber - 1);
//        if (pageNumber < totalPages)
//            modelAndView.addObject("nextPageUrl", pageNumber + 1);
//
//        modelAndView.addObject("pageContent", page.getContent());
//        modelAndView.addObject("title", "Main page");
//
//        return modelAndView;
//    }


}
