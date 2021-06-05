package com.blockwit.bwf.model.mapping;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.model.tasks.SchedTask;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.service.chains.common.utils.WithOptionsProfiledResult;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.Collection;
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
    return WithOptionsProfiledResult.process(optionService,
        StreamEx.of(Chains.values()).map(chain ->
            List.of(chain + OptionService.OPTION_Chain_ADDRESS_LINK,
                chain + OptionService.OPTION_Chain_BLOCK_NUMBER_LINK,
                chain + OptionService.OPTION_Chain_TX_HASH_LINK)).flatMap(Collection::stream).toList(),
        options -> Optional.of(StreamEx.of(tasks)
            .map(t -> map(t, context))
            .filter(Optional::isPresent)
            .map(Optional::get).toList())).get();
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
