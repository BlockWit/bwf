/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.mapping.MediaViewMapper;
import com.blockwit.bwf.repository.AccountRepository;
import com.blockwit.bwf.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/panel/media")
public class MediaController {

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  MediaService mediaService;

  @Autowired
  MediaViewMapper mediaViewMapper;

  @GetMapping
  public ModelAndView appPanelMedia() {
    return new ModelAndView("redirect:/panel/media/page/1");
  }

  @GetMapping("/page/{pageNumber}")
  public ModelAndView appPanelMediaPage(@PathVariable("pageNumber") int pageNumber) {
    return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/media/media"),
        pageNumber,
        pageRequest -> mediaService.findAllMediaPageable(pageRequest),
        mediaViewMapper);
  }

}
