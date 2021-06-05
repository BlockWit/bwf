package com.blockwit.bwf.service.chains.common.utils;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.model.tasks.SchedTask;
import com.blockwit.bwf.model.tasks.SchedTaskStatus;
import com.blockwit.bwf.service.SchedTasksService;
import com.blockwit.bwf.service.chains.common.IChainServicePool;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@Slf4j
public class PerformSchedTask {

  public static void process(AtomicBoolean destroy,
                             AtomicBoolean destroyFinished,
                             SchedTasksService schedTasksService,
                             String taskName,
                             IChainServicePool bscServicePool,
                             IChainServicePool ethServicePool,
                             ISchedTaskRun schedTaskRun) {

    if (destroyFinished.get())
      return;

    if (destroy.get()) {
      destroyFinished.set(true);
      return;
    }

    schedTasksService.findByName(taskName).ifPresentOrElse(task -> {
      if (task.getTaskStatus().equals(SchedTaskStatus.STS_RUNNING)) {
        WithDurationsInfo.process(task.getTaskDescr(), () ->
            WithChainService.process(ethServicePool, oppositeChainService ->
                WithChainService.process(bscServicePool, chainService ->
                    schedTaskRun.process(chainService, oppositeChainService))));
      } else if (task.getTaskStatus().equals(SchedTaskStatus.STS_PREPARING_STOP)) {
        if (schedTasksService.save(task.updateStatus(SchedTaskStatus.STS_STOPPED)).isEmpty())
          log.error("Can't update task status from " + task.getTaskStatus() +
              " to " + SchedTaskStatus.STS_STOPPED + " for task " + task.getTaskName());
      } else if (task.getTaskStatus().equals(SchedTaskStatus.STS_PREPARING_RUN)) {
        if (schedTasksService.save(task.updateStatus(SchedTaskStatus.STS_RUNNING)).isEmpty())
          log.error("Can't update task status from " + task.getTaskStatus() +
              " to " + SchedTaskStatus.STS_STOPPED + " for task " + task.getTaskName());
      } else if (task.getTaskStatus().equals(SchedTaskStatus.STS_STOPPED)) {
        // Nothing to do
      } else
        log.error("Task  " + task.getTaskName() + " have unknown status " + task.getTaskStatus());
    }, () -> log.error("Can't find task \"" + taskName + "\""));
  }

  public static void process(AtomicBoolean destroy,
                             AtomicBoolean destroyFinished,
                             SchedTasksService schedTasksService,
                             Chains chain,
                             String taskName,
                             IChainServicePool chainServicePool,
                             IChainServicePool oppositeChainServicePool,
                             ISchedTaskRun schedTaskRun) {

    if (destroyFinished.get())
      return;

    if (destroy.get()) {
      destroyFinished.set(true);
      return;
    }

    schedTasksService.findByName(taskName + chain.name()).ifPresentOrElse(task -> {
      if (task.getTaskStatus().equals(SchedTaskStatus.STS_RUNNING)) {
        WithDurationsInfo.process(task.getTaskDescr(), () ->
            WithChainService.process(oppositeChainServicePool, oppositeChainService ->
                WithChainService.process(chainServicePool, chainService ->
                    schedTaskRun.process(chainService, oppositeChainService))));
      } else if (task.getTaskStatus().equals(SchedTaskStatus.STS_PREPARING_STOP)) {
        if (schedTasksService.save(task.updateStatus(SchedTaskStatus.STS_STOPPED)).isEmpty())
          log.error("Can't update task status from " + task.getTaskStatus() +
              " to " + SchedTaskStatus.STS_STOPPED + " for task " + task.getTaskName());
      } else if (task.getTaskStatus().equals(SchedTaskStatus.STS_PREPARING_RUN)) {
        if (schedTasksService.save(task.updateStatus(SchedTaskStatus.STS_RUNNING)).isEmpty())
          log.error("Can't update task status from " + task.getTaskStatus() +
              " to " + SchedTaskStatus.STS_STOPPED + " for task " + task.getTaskName());
      } else if (task.getTaskStatus().equals(SchedTaskStatus.STS_STOPPED)) {
        // Nothing to do
      } else
        log.error("Task  " + task.getTaskName() + " have unknown status " + task.getTaskStatus());
    }, () -> log.error("Can't find task \"" + taskName + "\" for chain " + chain.name()));
  }

  public static boolean onlyRunning(
      AtomicBoolean destroy,
      AtomicBoolean destroyFinished,
      SchedTasksService schedTasksService,
      Chains chain,
      String taskName,
      Supplier<Boolean> f) {

    if (destroyFinished.get())
      return false;

    if (destroy.get()) {
      destroyFinished.set(true);
      return false;
    }

    Optional<SchedTask> schedTaskOpt = schedTasksService.findByName(taskName + chain.name());
    if (schedTaskOpt.isPresent()) {
      if (schedTaskOpt.get().getTaskStatus().equals(SchedTaskStatus.STS_RUNNING))
        return f.get();
      else
        return false;
    } else {
      log.error("Can't find task \"" + taskName + "\" for chain " + chain.name() + ". Return as stopping");
      return false;
    }
  }

}
