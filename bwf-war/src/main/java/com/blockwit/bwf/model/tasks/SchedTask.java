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
