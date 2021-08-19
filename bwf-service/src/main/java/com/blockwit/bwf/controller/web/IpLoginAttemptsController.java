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

package com.blockwit.bwf.controller.web;

import com.blockwit.bwf.controller.ControllerHelper;
import com.blockwit.bwf.model.mapping.IpLoginAttemptViewMapper;
import com.blockwit.bwf.repository.IpLoginAttemptsRepository;
import com.blockwit.bwf.service.IpLoginAttemptService;
import com.blockwit.bwf.service.utils.WithOptional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/panel/ip-login-attempts")
public class IpLoginAttemptsController {

  @Autowired
  IpLoginAttemptsRepository ipLoginAttemptsRepository;

  @Autowired
  IpLoginAttemptService ipLoginAttemptService;

  @Autowired
  IpLoginAttemptViewMapper ipLoginAttemptViewMapper;

  @GetMapping
  public ModelAndView appPanelSwaps() {
    return new ModelAndView("redirect:/panel/ip-login-attempts/page/1");
  }

  @GetMapping("/page/{pageNumber}")
  public ModelAndView appPanelSwapsPage(@PathVariable("pageNumber") int pageNumber) {
    return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/ip-login-attempts"),
        pageNumber,
        ipLoginAttemptsRepository,
        ipLoginAttemptViewMapper);
  }

  @GetMapping(value = "/ip-login-attempt/{ipLoginAttemptId}/delete")
  public ModelAndView removeTaskById(HttpServletRequest request,
                                     RedirectAttributes redirectAttributes,
                                     @PathVariable long ipLoginAttemptId) {
    return WithOptional.process(ipLoginAttemptService.deleteById(ipLoginAttemptId),
        () -> ControllerHelper.returnError400(request, redirectAttributes, "Can't delete IP login attempt with id " + ipLoginAttemptId + "! May be not found?")
        , ipLoginAttempt -> ControllerHelper.returnToReferer(request, redirectAttributes, "IP login attempt with id " + ipLoginAttemptId + " successfully deleted!")
    );
  }

}

