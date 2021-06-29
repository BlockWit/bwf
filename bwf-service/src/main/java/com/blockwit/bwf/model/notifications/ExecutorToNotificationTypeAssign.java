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

package com.blockwit.bwf.model.notifications;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "executors_to_notification_types_assigns")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ExecutorToNotificationTypeAssign implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, name = "apply_order")
  private Integer order;

  @Column(nullable = false)
  private Long executorId;

  @Column(nullable = false)
  private Long notificationTypeId;

  @Column(nullable = false)
  private boolean active;

  @Transient
  private NotificationType notificationType;

  @Transient
  private NotificationExecutorDescr notificationExecutor;

}
