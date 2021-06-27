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

import com.blockwit.bwf.form.NewNotification;
import com.blockwit.bwf.model.notifications.mapping.NotificationViewMapper;
import com.blockwit.bwf.service.notifications.NotificationService;
import com.blockwit.bwf.validator.NewNotificationValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/panel/notifications")
public class NotificationsController {

  private static final String TP_NOTIFICATIONS_NEW = "panel/pages/notifications/notification-new";

  @Autowired
  NotificationService notificationService;

  @Autowired
  NotificationViewMapper notificationViewMapper;

  @Autowired
  NewNotificationValidator newNotificationValidator;

  @GetMapping
  public ModelAndView appPanelNotifications() {
    return new ModelAndView("redirect:/panel/notifications/page/1");
  }

  @GetMapping("/page/{pageNumber}")
  public ModelAndView appPanelNotificationsPage(@PathVariable("pageNumber") int pageNumber) {
    return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/notifications/notifications"),
        pageNumber,
        pageRequest -> notificationService.findAllNotificationsPageable(pageRequest),
        notificationViewMapper);
  }

  @GetMapping("/create")
  public ModelAndView createNotificationGET() {
    return new ModelAndView(TP_NOTIFICATIONS_NEW, Map.of("newNotification", new NewNotification()));
  }

  @PostMapping("/create")
  public ModelAndView createNotificationPOST(
      RedirectAttributes redirectAttributes,
      @ModelAttribute("newNotification") @Valid NewNotification newNotification,
      BindingResult bindingResult
  ) {
    log.debug("Perform new notification form checks");

    newNotificationValidator.validate(newNotification, bindingResult);
    if (bindingResult.hasErrors())
      return new ModelAndView(TP_NOTIFICATIONS_NEW, bindingResult.getModel(), HttpStatus.BAD_REQUEST);

    log.info("Create notification");
    return notificationService.createTestNotification(newNotification.getLogin(), newNotification.getFieldContent()).fold(
        error -> ControllerHelper.returnError400(bindingResult, TP_NOTIFICATIONS_NEW, error.getDescr())
        , notification -> ControllerHelper.returnSuccess(redirectAttributes, "redirect:/panel/notifications/page/1",
            "Notification " + notification.getId() + " for " + notification.getAccount().getLogin() + " successfully created!")
    );
  }


}
