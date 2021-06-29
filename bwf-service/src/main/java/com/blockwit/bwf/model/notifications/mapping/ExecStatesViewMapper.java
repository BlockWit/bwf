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

package com.blockwit.bwf.model.notifications.mapping;

import com.blockwit.bwf.model.mapping.IMapper;
import com.blockwit.bwf.model.notifications.NotificationExecutorState;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ExecStatesViewMapper implements IMapper<NotificationExecutorState, ExecStateView> {

  @Override
  public List<ExecStateView> map(List<NotificationExecutorState> executorStates, Object context) {
    return StreamEx.of(executorStates)
        .map(t -> map(t, context))
        .filter(Optional::isPresent)
        .map(Optional::get).toList();
  }

  @Override
  public Optional<ExecStateView> map(NotificationExecutorState target, Object context) {
    return fromModelToView(target);
  }

  private static Optional<ExecStateView> fromModelToView(NotificationExecutorState model) {
    return Optional.of(ExecStateView.builder()
        .id(model.getId())
        .content(model.getNotification().getContent())
        .created(model.getCreated() == null ? "" : dateTimeFormatter.print(model.getCreated()))
        .updated(model.getUpdated() == null ? "" : dateTimeFormatter.print(model.getUpdated()))
        .sentDatetime(model.getSentDatetime() == null ? "" : dateTimeFormatter.print(model.getSentDatetime()))
        .log(model.getLog())
        .target(model.getNotification().getAccount().getLogin())
        .executorNotificationStateStatus(model.getExecutorNotificationStateStatus())
        .notificationExecutorId(model.getNotificationExecutorId())
        .notificationExecutorName(model.getNotificationExecutor().getName())
        .notificationId(model.getNotificationId())
        .notificationTypeName(model.getNotification().getNotificationType().getName())
        .build());
  }

}
