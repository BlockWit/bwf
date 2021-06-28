/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.service;

import com.blockwit.bwf.model.Error;
import com.blockwit.bwf.model.tasks.SchedTask;
import com.blockwit.bwf.model.tasks.SchedTaskStatus;
import com.blockwit.bwf.model.tasks.SchedTaskType;
import com.blockwit.bwf.model.tasks.SystemSchedTasks;
import com.blockwit.bwf.repository.SchedTasksRepository;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class SchedTasksService {

  private final SchedTasksRepository schedTasksRepository;

  public SchedTasksService(SchedTasksRepository schedTasksRepository) {
    this.schedTasksRepository = schedTasksRepository;

    List<SchedTask> defaultScheds = new ArrayList<>();

    defaultScheds.addAll(List.of(
        new SchedTask(0L, SystemSchedTasks.TASK_ID_COMMON_INFO_UPDATER, SystemSchedTasks.TASK_DESCR_COMMON_INFO_UPDATER, SchedTaskType.STT_SYSTEM, SchedTaskStatus.STS_STOPPED, null),
        new SchedTask(0L, SystemSchedTasks.TASK_ID_NOTIFICATIONS_PERFORMER, SystemSchedTasks.TASK_DESCR_NOTIFICATIONS_PERFORMER, SchedTaskType.STT_SYSTEM, SchedTaskStatus.STS_STOPPED, null)
    ));

    Set<String> tasksInRepo = StreamEx.of(schedTasksRepository.findByTaskNameIn(StreamEx.of(defaultScheds)
        .map(SchedTask::getTaskName)
        .toList())).map(SchedTask::getTaskName).toSet();

    defaultScheds.forEach(task -> {
      if (!tasksInRepo.contains(task.getTaskName())) schedTasksRepository.save(task);
    });

  }

  public Optional<SchedTask> findByName(String name) {
    return schedTasksRepository.findByTaskName(name);
  }

  public Set<SchedTask> findByNameIn(List<String> optionNames) {
    return schedTasksRepository.findByTaskNameIn(optionNames);
  }

  @Transactional
  public Optional<SchedTask> save(SchedTask taskStatus) {
    return Optional.ofNullable(schedTasksRepository.save(taskStatus));
  }

  @Transactional
  public Either<Error, List<SchedTask>> tryChangeStatus(List<Long> taskIds, SchedTaskStatus oldSchedTaskStatus, SchedTaskStatus newSchedTaskStatus) {
    List<SchedTask> list = schedTasksRepository.findByIdIn(taskIds);

    List<SchedTask> tasksToUpdate = new ArrayList<>();
    for (Long taskId : taskIds) {
      Optional<SchedTask> schedTaskOptional = StreamEx.of(list).findFirst(t -> t.getId().equals(taskId));
      if (schedTaskOptional.isPresent()) {
        SchedTask schedTask = schedTaskOptional.get();
        if (schedTask.getTaskStatus().equals(oldSchedTaskStatus)) {
          tasksToUpdate.add(schedTask.updateStatus(newSchedTaskStatus));
        } else {
          return Either.left(new Error(Error.EC_WRONG_TASK_STATUS, "Wrong task status " + schedTask.getTaskStatus() +
              ". It should be + " + oldSchedTaskStatus));
        }
      } else
        return Either.left(new Error(Error.EC_TASK_NOT_FOUND, "Task with id " + taskId + " not found"));
    }

    List<SchedTask> updatedTasks = schedTasksRepository.saveAll(tasksToUpdate);
    if (updatedTasks.size() != tasksToUpdate.size())
      return Either.left(new Error(Error.EC_CAN_NOT_UPDATE_TASK, "Unknown error during tasks update " +
          String.join(", ", StreamEx.of(taskIds).map(t -> t.toString()).toList()) +
          " update status from " + oldSchedTaskStatus + " to " + newSchedTaskStatus));

    return Either.right(updatedTasks);
  }

  @Transactional
  public Either<Error, List<SchedTask>> tryChangeStatusAll(SchedTaskStatus oldSchedTaskStatus, SchedTaskStatus newSchedTaskStatus) {
    List<SchedTask> list = schedTasksRepository.findAll();

    List<SchedTask> tasksToUpdate = new ArrayList<>();

    for (SchedTask schedTask : list) {
      if (schedTask.getTaskStatus().equals(oldSchedTaskStatus)) {
        tasksToUpdate.add(schedTask.updateStatus(newSchedTaskStatus));
      } else {
        if ((newSchedTaskStatus.equals(SchedTaskStatus.STS_PREPARING_STOP)
            && (schedTask.getTaskStatus().equals(SchedTaskStatus.STS_PREPARING_STOP) || schedTask.getTaskStatus().equals(SchedTaskStatus.STS_STOPPED))) ||
            (newSchedTaskStatus.equals(SchedTaskStatus.STS_PREPARING_RUN)
                && (schedTask.getTaskStatus().equals(SchedTaskStatus.STS_PREPARING_RUN) || schedTask.getTaskStatus().equals(SchedTaskStatus.STS_RUNNING)))) {
          tasksToUpdate.add(schedTask.updateStatus(newSchedTaskStatus));
        } else {
          return Either.left(new Error(Error.EC_WRONG_TASK_STATUS, "Wrong task status " + schedTask.getTaskStatus() +
              ". It should be + " + oldSchedTaskStatus));
        }
      }
    }

    List<SchedTask> updatedTasks = schedTasksRepository.saveAll(tasksToUpdate);
    if (updatedTasks.size() != tasksToUpdate.size())
      return Either.left(new Error(Error.EC_CAN_NOT_UPDATE_TASK, "Unknown error during tasks update " +
          String.join(", ", StreamEx.of(tasksToUpdate).map(t -> t.getId().toString()).toList()) +
          " update status from " + oldSchedTaskStatus + " to " + newSchedTaskStatus));

    return Either.right(updatedTasks);
  }


  @Transactional
  public Either<Error, SchedTask> tryChangeStatus(long taskId, SchedTaskStatus oldSchedTaskStatus, SchedTaskStatus
      newSchedTaskStatus) {
    Optional<SchedTask> schedTaskOptional = schedTasksRepository.findById(taskId);
    if (schedTaskOptional.isPresent()) {
      SchedTask schedTask = schedTaskOptional.get();
      if (schedTask.getTaskStatus().equals(oldSchedTaskStatus)) {
        SchedTask updatedTask = schedTasksRepository.save(schedTask.updateStatus(newSchedTaskStatus));
        if (updatedTask == null)
          return Either.left(new Error(Error.EC_CAN_NOT_UPDATE_TASK, "Unknown error during task " + taskId +
              " update status from " + oldSchedTaskStatus + " to " + newSchedTaskStatus));
        return Either.right(updatedTask);
      } else {
        return Either.left(new Error(Error.EC_WRONG_TASK_STATUS, "Wrong task status " + schedTask.getTaskStatus() +
            ". It should be + " + oldSchedTaskStatus));
      }
    }

    return Either.left(new Error(Error.EC_TASK_NOT_FOUND, "Task with id " + taskId + " not found"));
  }

}
