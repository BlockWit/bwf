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

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class OptionalExecutor<T> {

  public static <T> ModelAndView perform(String targetName,
                                         long targetId,
                                         JpaRepository repository,
                                         HttpServletRequest request,
                                         RedirectAttributes redirectAttributes,
                                         Function<T, ModelAndView> nonEmptyExecutor) {
    return perform(targetName,
        targetId,
        repository.findById(targetId),
        request,
        redirectAttributes,
        nonEmptyExecutor);
  }

  public static <T> ModelAndView perform(String targetName,
                                         long targetId,
                                         Optional<T> targetOpt,
                                         HttpServletRequest request,
                                         RedirectAttributes redirectAttributes,
                                         Function<T, ModelAndView> nonEmptyExecutor) {
    if (targetOpt.isPresent())
      return nonEmptyExecutor.apply(targetOpt.get());
    return ControllerHelper.returnToReferer(request,
        redirectAttributes,
        targetName + " with Id " + targetId + " not found!");
  }

}
