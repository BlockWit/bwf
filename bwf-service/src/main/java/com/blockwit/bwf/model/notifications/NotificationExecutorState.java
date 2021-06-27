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

@Getter
@Setter
@Entity
@Table(name = "notification_executor_state")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NotificationExecutorState {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private Long notificationId;

  @Column(nullable = false)
  private String notificationExecutorName;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private ExecutorNotificationStateStatus executorNotificationStateStatus;

  @Column
  private Long sentDatetime;

  @Column(nullable = false)
  private Long created;

  @Column
  private Long updated;

  @Column
  private String log;

}
