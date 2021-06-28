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
public class NotificationService {

  private final NotificationsRepository notificationsRepository;

  private final AccountRepository accountRepository;

  private final NotificationTypesRepository notificationTypesRepository;

  private final NotificationExecutorsRepository notificationExecutorsRepository;

  private final ExecutorsToNotificationsTypeAssignRepository executorsToNotificationsTypeAssignRepository;

  public NotificationService(NotificationsRepository notificationsRepository,
                             AccountRepository accountRepository,
                             NotificationTypesRepository notificationTypesRepository,
                             NotificationExecutorsRepository notificationExecutorsRepository,
                             ExecutorsToNotificationsTypeAssignRepository executorsToNotificationsTypeAssignRepository
  ) {
    this.notificationTypesRepository = notificationTypesRepository;
    this.accountRepository = accountRepository;
    this.notificationsRepository = notificationsRepository;
    this.notificationExecutorsRepository = notificationExecutorsRepository;
    this.executorsToNotificationsTypeAssignRepository = executorsToNotificationsTypeAssignRepository;

    Map<String, String> notificationsMap = new HashMap();

    notificationsMap.put(NotificationTypeConstants.NN_TEST, NotificationTypeConstants.ND_TEST);

    initializeNotifications(notificationsMap);
  }

  @Transactional
  protected void initializeNotifications(Map<String, String> notificationsMap) {
    Set<String> defaultNames = StreamEx.of(notificationsMap.entrySet()).map(t -> t.getKey()).toSet();
    Map<String, String> namesToDescrs = notificationTypesRepository.findByNameIn(defaultNames)
        .stream().collect(Collectors.toMap(NotificationType::getName, NotificationType::getDescr));

    if (!namesToDescrs.keySet().containsAll(defaultNames)) {
      List<NotificationType> toSave = new ArrayList<>();
      for (String name : defaultNames) {
        if (!namesToDescrs.containsKey(name)) {
          toSave.add(new NotificationType(0L, name, notificationsMap.get(name)));
        }
      }

      notificationTypesRepository.saveAll(toSave);
    }
  }

  public Page<Notification> findAllNotificationsPageable(PageRequest pageRequest) {
    Page<Notification> page = notificationsRepository.findAll(pageRequest);

    List<Notification> content = page.getContent();
    List<Account> accounts = accountRepository.findAllById(
        StreamEx.of(content)
            .map(t -> t.getTargetId())
            .distinct()
            .toList());

    List<NotificationType> notificationTypes = notificationTypesRepository.findAllById(
        StreamEx.of(content)
            .map(t -> t.getNotificationTypeId())
            .distinct()
            .toList());

    return new PageImpl(StreamEx.of(content)
        .map(notification -> notification.toBuilder()
            .notificationType(StreamEx.of(notificationTypes).findFirst(t -> t.getId().equals(notification.getNotificationTypeId())).get())
            .account(StreamEx.of(accounts).findFirst(t -> t.getId().equals(notification.getTargetId())).get())
            .build())
        .toList());
  }

  @Transactional
  public Either<Error, Notification> createTestNotification(String login, String content) {
    return WithOptional.process(accountRepository.findByLogin(login),
        () -> Either.left(new Error(Error.EC_ACCOUNT_NOT_FOUND, Error.EM_ACCOUNT_NOT_FOUND + ": " + login)),
        account -> WithOptional.process(notificationTypesRepository.findByName(NotificationTypeConstants.NN_TEST),
            () -> Either.left(new Error(Error.EC_NOTIFICATION_TYPE_NOT_FOUND, Error.EM_NOTIFICATION_TYPE_NOT_FOUND + ": " + NotificationTypeConstants.NN_TEST)),
            notificationType ->
                Either.right(notificationsRepository.save((Notification.builder()
                    .created(System.currentTimeMillis())
                    .notificationStatus(NotificationStatus.WAIT_FOR_PREPARE)
                    .notificationType(notificationType)
                    .notificationTypeId(notificationType.getId())
                    .notificationSendingType(NotificationSendingType.IMMEDIATELLY)
                    .content(content)
                    .targetId(account.getId())
                    .build())).toBuilder()
                    .account(account)
                    .notificationType(notificationType)
                    .build())));
  }

  @Transactional
  public Either<Error, ExecutorToNotificationTypeAssign> createNotificationAssign(
      String notificationTypeName,
      String notificationExecutorName,
      Integer order) {
    return WithOptional.process(notificationTypesRepository.findByName(notificationTypeName),
        () -> Either.left(new Error(Error.EC_NOTIFICATION_TYPE_NOT_FOUND, Error.EM_NOTIFICATION_TYPE_NOT_FOUND + ": " + notificationTypeName)),
        notificationType -> WithOptional.process(notificationExecutorsRepository.findByName(notificationExecutorName),
            () -> Either.left(new Error(Error.EC_NOTIFICATION_EXECUTOR_NOT_FOUND, Error.EM_NOTIFICATION_EXECUTOR_NOT_FOUND + ": " + notificationExecutorName)),
            notificationExecutor ->
                Either.right(executorsToNotificationsTypeAssignRepository.save(ExecutorToNotificationTypeAssign.builder()
                    .notificationType(notificationType)
                    .notificationExecutor(notificationExecutor)
                    .notificationTypeId(notificationType.getId())
                    .executorId(notificationExecutor.getId())
                    .order(order)
                    .build()).toBuilder()
                    .notificationType(notificationType)
                    .notificationExecutor(notificationExecutor)
                    .build())));
  }


}

