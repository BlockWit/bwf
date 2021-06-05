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

package com.blockwit.bwf.service.utils;

import com.blockwit.bwf.model.tasks.SchedTask;
import com.blockwit.bwf.model.tasks.SchedTaskStatus;
import com.blockwit.bwf.service.SchedTasksService;
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
            schedTaskRun.process());
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

  public static boolean onlyRunning(
      AtomicBoolean destroy,
      AtomicBoolean destroyFinished,
      SchedTasksService schedTasksService,
      String taskName,
      Supplier<Boolean> f) {

    if (destroyFinished.get())
      return false;

    if (destroy.get()) {
      destroyFinished.set(true);
      return false;
    }

    Optional<SchedTask> schedTaskOpt = schedTasksService.findByName(taskName);
    if (schedTaskOpt.isPresent()) {
      if (schedTaskOpt.get().getTaskStatus().equals(SchedTaskStatus.STS_RUNNING))
        return f.get();
      else
        return false;
    } else {
      log.error("Can't find task \"" + taskName + "\". Return as stopping");
      return false;
    }
  }

}
