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
import com.blockwit.bwf.model.notifications.NotificationExecutorDescr;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class NotificationExecutorViewMapper implements IMapper<NotificationExecutorDescr, NotificationExecutorView> {

  @Override
  public List<NotificationExecutorView> map(List<NotificationExecutorDescr> notifications, Object context) {
    return StreamEx.of(notifications)
        .map(t -> map(t, context))
        .filter(Optional::isPresent)
        .map(Optional::get).toList();
  }

  @Override
  public Optional<NotificationExecutorView> map(NotificationExecutorDescr target, Object context) {
    return fromModelToView(target);
  }

  private static Optional<NotificationExecutorView> fromModelToView(NotificationExecutorDescr model) {
    return Optional.of(NotificationExecutorView.builder()
        .id(model.getId())
        .id(model.getId())
        .name(model.getName())
        .description(model.getDescription())
        .build());
  }

}
