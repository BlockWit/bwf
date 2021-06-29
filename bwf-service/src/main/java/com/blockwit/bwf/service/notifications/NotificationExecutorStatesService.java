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

import com.blockwit.bwf.model.account.Account;
import com.blockwit.bwf.model.notifications.Notification;
import com.blockwit.bwf.model.notifications.NotificationExecutorDescr;
import com.blockwit.bwf.model.notifications.NotificationExecutorState;
import com.blockwit.bwf.model.notifications.NotificationType;
import com.blockwit.bwf.repository.AccountRepository;
import com.blockwit.bwf.repository.notifications.NotificationExecutorStatesRepository;
import com.blockwit.bwf.repository.notifications.NotificationExecutorsRepository;
import com.blockwit.bwf.repository.notifications.NotificationTypesRepository;
import com.blockwit.bwf.repository.notifications.NotificationsRepository;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationExecutorStatesService {

  @Autowired
  private NotificationExecutorStatesRepository notificationExecutorStatesRepository;

  @Autowired
  private NotificationExecutorsRepository notificationExecutorsRepository;

  @Autowired
  private NotificationsRepository notificationsRepository;

  @Autowired
  private NotificationTypesRepository notificationTypesRepository;

  @Autowired
  private AccountRepository accountRepository;

  public Page<NotificationExecutorState> findAllAssignsPageable(PageRequest pageRequest) {
    Page<NotificationExecutorState> page = notificationExecutorStatesRepository.findAll(pageRequest);

    List<NotificationExecutorState> content = page.getContent();

    List<NotificationExecutorDescr> notificationExecutors = notificationExecutorsRepository.findAllById(
        StreamEx.of(content)
            .map(t -> t.getNotificationExecutorId())
            .distinct()
            .toList());

    List<Notification> notifications = notificationsRepository.findAllById(
        StreamEx.of(content)
            .map(t -> t.getNotificationId())
            .distinct()
            .toList());

    List<NotificationType> notificationTypes = notificationTypesRepository.findAllById(
        StreamEx.of(notifications)
            .map(t -> t.getNotificationTypeId())
            .distinct()
            .toList());

    List<Account> accounts = accountRepository.findAllById(
        StreamEx.of(notifications)
            .map(t -> t.getTargetId())
            .distinct()
            .toList());

    List<Notification> notificationsFull = StreamEx.of(notifications)
        .map(t -> t.toBuilder()
            .account(accounts.stream().filter(a -> a.getId().equals(t.getTargetId())).findAny().get())
            .notificationType(notificationTypes.stream().filter(a -> a.getId().equals(t.getNotificationTypeId())).findAny().get())
            .build())
        .toList();

    return new PageImpl(StreamEx.of(content)
        .map(assign -> assign.toBuilder()
            .notification(StreamEx.of(notificationsFull).findFirst(t -> t.getId().equals(assign.getNotificationId())).get())
            .notificationExecutor(StreamEx.of(notificationExecutors).findFirst(t -> t.getId().equals(assign.getNotificationExecutorId())).get())
            .build())
        .toList(),
        page.getPageable(),
        page.getTotalElements());
  }

  @Transactional
  public List<Notification> createNewStatesForNotifications(List<Notification> notifications, List<NotificationExecutorState> states) {
    List<Notification> updatedNotifications = notificationsRepository.saveAll(notifications);
    List<NotificationExecutorState> updatedNotificationExecutorStates = notificationExecutorStatesRepository.saveAll(states);
    return StreamEx.of(updatedNotifications).map(
        n -> n.toBuilder()
            .notificationExecutorStates(StreamEx.of(updatedNotificationExecutorStates)
                .filter(s -> s.getNotificationId().equals(n.getId()))
                .toList())
            .build()
    ).toList();
  }

}

