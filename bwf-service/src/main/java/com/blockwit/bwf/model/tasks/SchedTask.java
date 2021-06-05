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

package com.blockwit.bwf.model.tasks;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "sched_tasks")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SchedTask implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, unique = true)
  private String taskName;

  @Column
  private String taskDescr;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private SchedTaskType taskType;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private SchedTaskStatus taskStatus;

  @Column
  private String log;

  public SchedTask updateStatus(SchedTaskStatus schedTaskStatus) {
    return this.toBuilder()
        .taskStatus(schedTaskStatus)
        .log(null)
        .build();
  }

  public SchedTask updateStatus(SchedTaskStatus schedTaskStatus, String log) {
    return this.toBuilder()
        .taskStatus(schedTaskStatus)
        .log(log)
        .build();
  }


}
