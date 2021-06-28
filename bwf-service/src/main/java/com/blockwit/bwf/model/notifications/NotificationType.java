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
@Table(name = "notification_types")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NotificationType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column
  private String descr;

}
