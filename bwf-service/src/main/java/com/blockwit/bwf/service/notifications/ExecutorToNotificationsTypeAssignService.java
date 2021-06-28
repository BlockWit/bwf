/*
 * Copyright (c) 2017-present BlockWit, LLC. All rights reserved.
 *
 *  This library is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation; either version 2.1 of the License, or (at your option)
 *  any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 *  details.
 *
 */

package com.blockwit.bwf.service.notifications;

import com.blockwit.bwf.model.Error;
import com.blockwit.bwf.model.account.Account;
import com.blockwit.bwf.model.notifications.*;
import com.blockwit.bwf.repository.AccountRepository;
import com.blockwit.bwf.repository.notifications.ExecutorsToNotificationsTypeAssignRepository;
import com.blockwit.bwf.repository.notifications.NotificationExecutorsRepository;
import com.blockwit.bwf.repository.notifications.NotificationTypesRepository;
import com.blockwit.bwf.repository.notifications.NotificationsRepository;
import com.blockwit.bwf.service.utils.WithOptional;
import io.vavr.control.Either;
import one.util.streamex.StreamEx;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExecutorToNotificationsTypeAssignService {


  private final ExecutorsToNotificationsTypeAssignRepository executorsToNotificationsTypeAssignRepository;

  private final NotificationTypesRepository notificationTypesRepository;

  private final NotificationExecutorsRepository notificationExecutorsRepository;

  public ExecutorToNotificationsTypeAssignService(
      ExecutorsToNotificationsTypeAssignRepository executorsToNotificationsTypeAssignRepository,
      NotificationTypesRepository notificationTypesRepository,
      NotificationExecutorsRepository notificationExecutorsRepository
  ) {
    this.executorsToNotificationsTypeAssignRepository = executorsToNotificationsTypeAssignRepository;
    this.notificationTypesRepository = notificationTypesRepository;
    this.notificationExecutorsRepository = notificationExecutorsRepository;
//    Map<String, String> notificationsMap = new HashMap();
//
//    notificationsMap.put(NotificationTypeConstants.NN_TEST, NotificationTypeConstants.ND_TEST);
//
//    initializeNotifications(notificationsMap);
  }

//  @Transactional
//  protected void initializeNotifications(Map<String, String> notificationsMap) {
//    Set<String> defaultNames = StreamEx.of(notificationsMap.entrySet()).map(t -> t.getKey()).toSet();
//    Map<String, String> namesToDescrs = notificationTypesRepository.findByNameIn(defaultNames)
//        .stream().collect(Collectors.toMap(NotificationType::getName, NotificationType::getDescr));
//
//    if (!namesToDescrs.keySet().containsAll(defaultNames)) {
//      List<NotificationType> toSave = new ArrayList<>();
//      for (String name : defaultNames) {
//        if (!namesToDescrs.containsKey(name)) {
//          toSave.add(new NotificationType(0L, name, notificationsMap.get(name)));
//        }
//      }
//
//      notificationTypesRepository.saveAll(toSave);
//    }
//  }

  public Page<ExecutorToNotificationTypeAssign> findAllAssignsPageable(PageRequest pageRequest) {
    Page<ExecutorToNotificationTypeAssign> page = executorsToNotificationsTypeAssignRepository.findAll(pageRequest);

    List<ExecutorToNotificationTypeAssign> content = page.getContent();
    List<NotificationExecutorDescr> notificationExecutors = notificationExecutorsRepository.findAllById(
        StreamEx.of(content)
            .map(t -> t.getExecutorId())
            .distinct()
            .toList());

    List<NotificationType> notificationTypes = notificationTypesRepository.findAllById(
        StreamEx.of(content)
            .map(t -> t.getNotificationTypeId())
            .distinct()
            .toList());

    return new PageImpl(StreamEx.of(content)
        .map(assign -> assign.toBuilder()
            .notificationType(StreamEx.of(notificationTypes).findFirst(t -> t.getId().equals(assign.getNotificationTypeId())).get())
            .notificationExecutor(StreamEx.of(notificationExecutors).findFirst(t -> t.getId().equals(assign.getExecutorId())).get())
            .build())
        .toList());
  }

}

