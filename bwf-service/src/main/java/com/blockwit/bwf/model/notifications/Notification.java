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

import com.blockwit.bwf.model.account.Account;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Notification implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private Long notificationTypeId;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private NotificationSendingType notificationSendingType;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private NotificationStatus notificationStatus;

  @Column(nullable = false)
  private Long targetId;

  @Column(nullable = false)
  private String content;

  @Column
  private Long sentDatetime;

  @Column
  private Long plannedDatetime;

  @Column(nullable = false)
  private Long created;

  @Column
  private Long updated;

  @Column
  private String log;


  @Transient
  private Account account;

  @Transient
  private NotificationType notificationType;

  @Transient
  private List<NotificationExecutorState> notificationExecutorStates;

  public Notification update(NotificationStatus notificationStatus) {
    return toBuilder()
        .notificationStatus(notificationStatus)
        .build();
  }

}
