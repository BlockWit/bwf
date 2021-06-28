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
import com.blockwit.bwf.form.NewNotificationAssign;
import com.blockwit.bwf.model.notifications.mapping.AssignsViewMapper;
import com.blockwit.bwf.model.notifications.mapping.NotificationExecutorViewMapper;
import com.blockwit.bwf.model.notifications.mapping.NotificationTypeViewMapper;
import com.blockwit.bwf.model.notifications.mapping.NotificationViewMapper;
import com.blockwit.bwf.repository.notifications.ExecutorsToNotificationsTypeAssignRepository;
import com.blockwit.bwf.repository.notifications.NotificationExecutorsRepository;
import com.blockwit.bwf.repository.notifications.NotificationTypesRepository;
import com.blockwit.bwf.service.notifications.ExecutorToNotificationsTypeAssignService;
import com.blockwit.bwf.service.notifications.NotificationService;
import com.blockwit.bwf.validator.NewNotificationAssignValidator;
import com.blockwit.bwf.validator.NewNotificationValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/panel/notifications")
public class NotificationsController {

  private static final String TP_NOTIFICATIONS_NEW = "panel/pages/notifications/notification-new";

  private static final String TP_NOTIFICATION_ASSIGN_NEW = "panel/pages/notifications/assign-new";

  @Autowired
  AssignsViewMapper assignsViewMapper;

  @Autowired
  NotificationService notificationService;

  @Autowired
  NotificationTypesRepository notificationTypesRepository;

  @Autowired
  NotificationExecutorsRepository notificationExecutorsRepository;

  @Autowired
  ExecutorsToNotificationsTypeAssignRepository executorsToNotificationsTypeAssignRepository;

  @Autowired
  ExecutorToNotificationsTypeAssignService executorToNotificationsTypeAssignService;

  @Autowired
  NotificationViewMapper notificationViewMapper;

  @Autowired
  NotificationTypeViewMapper notificationTypeMapper;

  @Autowired
  NotificationExecutorViewMapper notificationExecutorViewMapper;

  @Autowired
  NewNotificationValidator newNotificationValidator;

  @Autowired
  NewNotificationAssignValidator newNotificationAssignValidator;

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

  @GetMapping("/parts/executors")
  public ModelAndView appPanelExecutors() {
    return new ModelAndView("redirect:/panel/notifications/parts/executors/page/1");
  }

  @GetMapping("/parts/executors/page/{pageNumber}")
  public ModelAndView appPanelExecutorsPage(@PathVariable("pageNumber") int pageNumber) {
    return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/notifications/executors"),
        pageNumber,
        notificationExecutorsRepository,
        notificationExecutorViewMapper);
  }

  @GetMapping("/parts/notifytypes")
  public ModelAndView appPanelNotificationTypes() {
    return new ModelAndView("redirect:/panel/notifications/parts/notifytypes/page/1");
  }

  @GetMapping("/parts/notifytypes/page/{pageNumber}")
  public ModelAndView appPanelNotificationTypesPage(@PathVariable("pageNumber") int pageNumber) {
    return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/notifications/notifytypes"),
        pageNumber,
        notificationTypesRepository,
        notificationTypeMapper);
  }

  @GetMapping("/parts/assigns")
  public ModelAndView appPanelExecutorsToNotifications() {
    return new ModelAndView("redirect:/panel/notifications/parts/assigns/page/1");
  }

  @GetMapping("/parts/assigns/page/{pageNumber}")
  public ModelAndView appPanelExecutorsToNotificationsPage(@PathVariable("pageNumber") int pageNumber) {
    return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/notifications/assigns"),
        pageNumber,
        pageRequest -> executorToNotificationsTypeAssignService.findAllAssignsPageable(pageRequest),
        assignsViewMapper);
  }

  @GetMapping("/parts/assigns/create")
  public ModelAndView createNotificationAssignGET() {
    return new ModelAndView(TP_NOTIFICATION_ASSIGN_NEW,
        Map.of("newNotificationAssign", new NewNotificationAssign(0, null, null),
        "notificationTypes", notificationTypesRepository.findAll(),
        "notificationExecutors", notificationExecutorsRepository.findAll()));
  }

  @PostMapping("/parts/assigns/create")
  public ModelAndView createNotificationAssignPOST(
      RedirectAttributes redirectAttributes,
      @ModelAttribute("newNotificationAssign") @Valid NewNotificationAssign newNotificationAssign,
      BindingResult bindingResult
  ) {
    log.debug("Perform new notification assign form checks");

    newNotificationAssignValidator.validate(newNotificationAssign, bindingResult);
    if (bindingResult.hasErrors()) {
      ModelAndView modelAndView = new ModelAndView(TP_NOTIFICATION_ASSIGN_NEW, bindingResult.getModel(), HttpStatus.BAD_REQUEST);
      modelAndView.addObject("notificationTypes", notificationTypesRepository.findAll());
      modelAndView.addObject("notificationExecutors", notificationExecutorsRepository.findAll());
      return modelAndView;
    }

    log.info("Create notification assign");

    return notificationService.createNotificationAssign(
        newNotificationAssign.getNotificationTypeName(),
        newNotificationAssign.getNotificationExecutorName(),
        newNotificationAssign.getOrder()).fold(
        error -> {
          ModelAndView modelAndView = new ModelAndView(TP_NOTIFICATION_ASSIGN_NEW, HttpStatus.BAD_REQUEST);
          modelAndView.addObject("notificationTypes", notificationTypesRepository.findAll());
          modelAndView.addObject("notificationExecutors", notificationExecutorsRepository.findAll());
          modelAndView.addObject("message_error", error.getDescr());
          return modelAndView;
        }
        , notificationAssign -> ControllerHelper.returnSuccess(redirectAttributes, "redirect:/panel/notifications/parts/assigns/page/1",
            "Notification assign " + notificationAssign.getId() +
                " for notification type " + notificationAssign.getNotificationType().getName() +
                " and executor " + notificationAssign.getNotificationExecutor().getName() + " successfully created!")
    );
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
