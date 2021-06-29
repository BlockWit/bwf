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
import com.blockwit.bwf.model.tasks.SystemSchedTasks;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.SchedTasksService;
import com.blockwit.bwf.service.notifications.executors.OutputToConsoleExecutor;
import com.blockwit.bwf.service.utils.PerformSchedTask;
import com.blockwit.bwf.service.utils.WhileExists;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationTasksService {

  @Autowired
  OutputToConsoleExecutor outputToConsoleExecutor;

  @Autowired
  private SchedTasksService schedTasksService;

  @Autowired
  private NotificationService notificationService;

  @Autowired
  private NotificationExecutorStatesService notificationExecutorStatesService;

  @Autowired
  private ExecutorToNotificationsTypeAssignService executorToNotificationsTypeAssignService;

  @Autowired
  OptionService optionService;

  private AtomicBoolean destroy = new AtomicBoolean(false);
  private AtomicBoolean destroyFinished = new AtomicBoolean(false);

  @PreDestroy
  public void waitDestroy() {
    log.info("Waiting for common updater processor destroy");
    destroy.set(true);
    while (!destroyFinished.get()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    log.info("Common updater processor destroy process finished");
  }

  @Scheduled(fixedDelay = 1000)
  protected void notificationsPerformer() {
    PerformSchedTask.process(destroy,
        destroyFinished,
        schedTasksService,
        SystemSchedTasks.TASK_ID_NOTIFICATIONS_PERFORMER,
        () -> WhileExists.process(
            () -> notificationService.findNotificationsByStatus(NotificationStatus.WAIT_FOR_PREPARE),
            notifications -> notificationExecutorStatesService.createNewStatesForNotifications(
                StreamEx.of(notifications)
                    .map(n -> n.update(NotificationStatus.WAIT_FOR_SENDING)).toList(),
                executorToNotificationsTypeAssignService
                    .findAssignsForNotificationTypes(StreamEx.of(notifications)
                        .map(t -> t.getNotificationTypeId())
                        .distinct().toList())
                    .stream()
                    .map(assign ->
                        StreamEx.of(notifications)
                            .filter(n -> n.getNotificationTypeId().equals(assign.getNotificationTypeId()))
                            .map(notification ->
                                NotificationExecutorState.builder()
                                    .executorNotificationStateStatus(ExecutorNotificationStateStatus.WAIT_FOR_SENDING)
                                    .created(System.currentTimeMillis())
                                    .notificationId(notification.getId())
                                    .notificationExecutorId(assign.getExecutorId())
                                    .build()
                            ).toList()
                    ).flatMap(Collection::stream).collect(Collectors.toList())
            )
        )
    );
  }

  @Scheduled(fixedDelay = 1000)
  protected void notificationsExecutor() {
    PerformSchedTask.process(destroy,
        destroyFinished,
        schedTasksService,
        SystemSchedTasks.TASK_ID_NOTIFICATIONS_EXECUTOR,
        () -> {
          WhileExists.process(
              () -> notificationService.findNotificationsByStatusWithStates(NotificationStatus.WAIT_FOR_SENDING),
              notifications ->
                  notifications.forEach(notification -> {
                    Notification notificationInProgress = notificationService.saveStatus(notification.update(NotificationStatus.SEND_IN_PROGRESS));

                    List<NotificationExecutorState> executorStates = StreamEx.of(notificationInProgress.getNotificationExecutorStates()).map(nes -> {
                      if (nes.getExecutorNotificationStateStatus().equals(ExecutorNotificationStateStatus.WAIT_FOR_SENDING)) {

                        NotificationExecutorState inProgressExecState = notificationService
                            .saveStatus(nes.toBuilder()
                                .executorNotificationStateStatus(ExecutorNotificationStateStatus.SENDING_IN_PROGRESS)
                                .updated(System.currentTimeMillis())
                                .build());

                        Optional<Error> errorOpt = processSend(inProgressExecState.getNotificationExecutor().getName(),
                            notification.getAccount(),
                            notification.getContent());

                        if (errorOpt.isPresent()) {
                          return notificationService
                              .saveStatus(inProgressExecState.toBuilder()
                                  .executorNotificationStateStatus(ExecutorNotificationStateStatus.FAIL)
                                  .updated(System.currentTimeMillis())
                                  .log(errorOpt.get().getDescr())
                                  .build());
                        } else {
                          return notificationService
                              .saveStatus(inProgressExecState.toBuilder()
                                  .executorNotificationStateStatus(ExecutorNotificationStateStatus.SENT)
                                  .sentDatetime(System.currentTimeMillis())
                                  .updated(System.currentTimeMillis())
                                  .build());
                        }
                      } else if (nes.getExecutorNotificationStateStatus().equals(ExecutorNotificationStateStatus.SENDING_IN_PROGRESS)) {
                        // Случай, когда вырубили комп во время отправки, в этом случае мы не знаем была ли выполнена отправка или нет.
                        // В этом случае мы не знаем нужно ли повторно отправлять - оставляем как есть - ручной выбор пока.
                        return nes;
                      } else {
                        // Когда у нас уже выполнена отправка FAIL или SENT - ранее
                        return nes;
                      }
                    }).toList();

                    boolean isOneSent = false;
                    boolean isOneFail = false;
                    boolean isOneInProgress = false;
                    boolean isWaitForSending = false;
                    boolean isFail = false;
                    for (NotificationExecutorState state : executorStates) {
                      if (state.getExecutorNotificationStateStatus().equals(ExecutorNotificationStateStatus.FAIL)) {
                        isOneFail = true;
                      } else if (state.getExecutorNotificationStateStatus().equals(ExecutorNotificationStateStatus.SENT)) {
                        isOneSent = true;
                      } else if (state.getExecutorNotificationStateStatus().equals(ExecutorNotificationStateStatus.SENDING_IN_PROGRESS)) {
                        isOneInProgress = true;
                      } else if (state.getExecutorNotificationStateStatus().equals(ExecutorNotificationStateStatus.WAIT_FOR_SENDING)) {
                        isWaitForSending = true;
                      } else {
                        isFail = true;
                      }
                    }

                    if (isFail) {
                      notificationService.saveStatus(notification.toBuilder()
                          .notificationStatus(NotificationStatus.FAIL)
                          .log("Unsupported status in states. Requires manual check and update!")
                          .updated(System.currentTimeMillis())
                          .build());
                    } else if (isOneInProgress) {
                      notificationService.saveStatus(notification.toBuilder()
                          .notificationStatus(NotificationStatus.FAIL)
                          .log("One notification execution state in progress. It looks like service have wrong shut down. You" +
                              " should check manually all states. And change state if it needs!")
                          .updated(System.currentTimeMillis())
                          .build());
                    } else if (isWaitForSending) {
                      notificationService.saveStatus(notification.toBuilder()
                          .notificationStatus(NotificationStatus.WAIT_FOR_SENDING)
                          .log("Some notifications still waiting for sending!")
                          .updated(System.currentTimeMillis())
                          .build());
                    } else if (isOneSent && isOneFail) {
                      notificationService.saveStatus(notification.toBuilder()
                          .notificationStatus(NotificationStatus.PARTIAL_FAIL)
                          .log("Some of notifications fails, some sent. In different cases it requires manual check!")
                          .updated(System.currentTimeMillis())
                          .build());
                    } else if (!isOneSent && isOneFail) {
                      notificationService.saveStatus(notification.toBuilder()
                          .notificationStatus(NotificationStatus.FAIL)
                          .log("All notifications fails!")
                          .updated(System.currentTimeMillis())
                          .build());
                    } else if (isOneSent && !isOneFail) {
                      notificationService.saveStatus(notification.toBuilder()
                          .notificationStatus(NotificationStatus.SENT)
                          .log("All notifications sent!")
                          .updated(System.currentTimeMillis())
                          .sentDatetime(System.currentTimeMillis())
                          .build());
                    } else if (!isOneSent && !isOneFail) {
                      notificationService.saveStatus(notification.toBuilder()
                          .notificationStatus(NotificationStatus.FAIL)
                          .log("No notifications statuses." +
                              " It looks like you have no executor assigns for corresponding notification type "
                              + notification.getNotificationType().getName())
                          .updated(System.currentTimeMillis())
                          .sentDatetime(System.currentTimeMillis())
                          .build());
                    }


                  })
          );
          WhileExists.process(
              () -> notificationService.findNotificationsByStatusWithStates(NotificationStatus.SEND_IN_PROGRESS),
              notifications ->
                  notificationService.saveStatus(StreamEx.of(notifications)
                      .map(t -> t.update(NotificationStatus.WAIT_FOR_SENDING))
                      .toList())
          );

        }
    );
  }

  private Optional<Error> processSend(
      String executorName,
      Account account,
      String content) {
    if (executorName.equals(NotificationExecutorConsts.NEI_CONSOLE)) {
      outputToConsoleExecutor.send(account.getLogin(), content);
      return Optional.empty();
    } else {
      return Optional.of(new Error(Error.EC_NOTIFICATION_EXECUTOR_NOT_FOUND,
          Error.EM_NOTIFICATION_EXECUTOR_NOT_FOUND + executorName +
              " during sending notification to account " + account.getLogin()));
    }
  }

}

