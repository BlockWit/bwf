/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.model.notifications.mapping;

import com.blockwit.bwf.model.notifications.NotificationSendingType;
import com.blockwit.bwf.model.notifications.NotificationStatus;
import com.blockwit.bwf.model.notifications.NotificationType;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Builder
public class NotificationView {

  private Long id;

  private NotificationSendingType notificationSendingType;

  private NotificationStatus notificationStatus;

  private String target;

  private String sentDatetime;

  private String plannedDatetime;

  private String created;

  private String updated;

  private String log;

  private String content;

  private String notificationType;

}
