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

import com.blockwit.bwf.service.AppUpdatableInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class AppController {

  @Autowired
  private AppUpdatableInfo appUpdatableInfo;

  @GetMapping("/")
  public String appHome(Model model) {
    model.addAllAttributes(appUpdatableInfo.getInfoMap());
    return "front/pages/home";
  }

  @GetMapping("/panel")
  public String panelHome(Model model) {
    model.addAllAttributes(appUpdatableInfo.getInfoMap());
    return "panel/pages/home";
  }

  @GetMapping("/panel/profile")
  public String panelProfile(Model model) {
    return "panel/pages/profile";
  }

}
