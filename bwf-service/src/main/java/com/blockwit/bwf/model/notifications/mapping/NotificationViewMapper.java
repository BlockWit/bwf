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
import com.blockwit.bwf.model.notifications.Notification;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class NotificationViewMapper implements IMapper<Notification, NotificationView> {

  @Override
  public List<NotificationView> map(List<Notification> notifications, Object context) {
    return StreamEx.of(notifications)
        .map(t -> map(t, context))
        .filter(Optional::isPresent)
        .map(Optional::get).toList();
  }

  @Override
  public Optional<NotificationView> map(Notification notification, Object context) {
    return fromModelToView(notification);
  }

  private static Optional<NotificationView> fromModelToView(Notification model) {
    return Optional.of(NotificationView.builder()
        .id(model.getId())
        .notificationStatus(model.getNotificationStatus())
        .notificationSendingType(model.getNotificationSendingType())
        .sentDatetime(model.getSentDatetime() == null ? "" : dateTimeFormatter.print(model.getSentDatetime()))
        .updated(model.getUpdated() == null ? "" : dateTimeFormatter.print(model.getUpdated()))
        .created(model.getCreated() == null ? "" : dateTimeFormatter.print(model.getCreated()))
        .plannedDatetime(model.getPlannedDatetime() == null ? "" : dateTimeFormatter.print(model.getPlannedDatetime()))
        .log(model.getLog())
        .notificationType(model.getNotificationType().getName())
        .target(model.getAccount().getLogin())
        .content(model.getContent())
        .build());
  }

}
