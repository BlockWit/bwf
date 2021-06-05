package com.blockwit.bwf.model.mapping;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskView {

  private Long id;

  private String taskName;

  private String taskDescr;

  private String taskType;

  private String taskStatus;

  private String log;

}
