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

import com.blockwit.bwf.model.notifications.NotificationExecutorConsts;
import com.blockwit.bwf.model.notifications.NotificationExecutorDescr;
import com.blockwit.bwf.repository.notifications.NotificationExecutorsRepository;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationExecutorsService {

  private final NotificationExecutorsRepository notificationExecutorsRepository;

  public NotificationExecutorsService(NotificationExecutorsRepository notificationExecutorsRepository
  ) {
    this.notificationExecutorsRepository = notificationExecutorsRepository;

    Map<String, String> map = new HashMap();

    map.put(NotificationExecutorConsts.NEI_CONSOLE, NotificationExecutorConsts.NED_CONSOLE);

    initializeNotifications(map);
  }

  @Transactional
  protected void initializeNotifications(Map<String, String> notificationsMap) {
    Set<String> defaultNames = StreamEx.of(notificationsMap.entrySet()).map(t -> t.getKey()).toSet();
    Map<String, String> namesToDescrs = notificationExecutorsRepository.findByNameIn(defaultNames)
        .stream().collect(Collectors.toMap(NotificationExecutorDescr::getName, NotificationExecutorDescr::getDescription));

    if (!namesToDescrs.keySet().containsAll(defaultNames)) {
      List<NotificationExecutorDescr> toSave = new ArrayList<>();
      for (String name : defaultNames) {
        if (!namesToDescrs.containsKey(name)) {
          toSave.add(new NotificationExecutorDescr(0L, name, notificationsMap.get(name)));
        }
      }

      notificationExecutorsRepository.saveAll(toSave);
    }
  }

}

