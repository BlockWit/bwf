package com.blockwit.bwf.model.mapping;

import com.blockwit.bwf.model.tasks.SchedTask;
import com.blockwit.bwf.service.OptionService;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskViewMapper implements IMapper<SchedTask, TaskView> {

  private final OptionService optionService;

  public TaskViewMapper(OptionService optionService) {
    this.optionService = optionService;
  }

  @Override
  public List<TaskView> map(List<SchedTask> tasks, Object context) {
    return StreamEx.of(tasks)
        .map(t -> map(t, context))
        .filter(Optional::isPresent)
        .map(Optional::get).toList();
  }

  @Override
  public Optional<TaskView> map(SchedTask task, Object context) {
    return fromModelToView(task);
  }

  private static Optional<TaskView> fromModelToView(SchedTask model) {
    return Optional.of(TaskView.builder()
        .id(model.getId())
        .taskName(model.getTaskName())
        .taskDescr(model.getTaskDescr())
        .taskType(model.getTaskType().name())
        .taskStatus(model.getTaskStatus().name())
        .log(model.getLog())
        .build());
  }

}
